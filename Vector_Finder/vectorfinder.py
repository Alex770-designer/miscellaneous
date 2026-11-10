
from pathlib import Path
import fitz
import csv
import json
import re

# ==========================================
# FOLDERS
# ==========================================

PROJECT_FOLDER = Path(__file__).resolve().parent.parent
PDF_FOLDER = PROJECT_FOLDER / "IB_Math_Papers"
OUTPUT_FOLDER = PROJECT_FOLDER / "Vector_Results"

OUTPUT_FOLDER.mkdir(exist_ok=True)

# ==========================================
# QUESTION DETECTION
# ==========================================

QUESTION_PATTERN = re.compile(
    r"^\s*(\d{1,2})\.\s*(.*?)\s*$"
)

def is_question_start(line):
    match = QUESTION_PATTERN.match(line)

    if match:
        return int(match.group(1)), match.group(2)

    return None, None


# ==========================================
# VECTOR DETECTION
# ==========================================

STRONG_PATTERNS = [
    r"\bposition vectors?\b",
    r"\bdirection vectors?\b",
    r"\bnormal vectors?\b",
    r"\bunit vectors?\b",
    r"\bscalar product\b",
    r"\bdot product\b",
    r"\bcross product\b",
    r"\bvector product\b",
    r"\bvector equation\b",
    r"\bvector equations\b",
    r"\bvector geometry\b",
    r"\bvector calculus\b",
    r"\bvector AB\b",
    r"\bvector BA\b",
    r"\bnon-zero vectors?\b",
    r"\bzero vector\b",
    r"\bparallel vectors?\b",
    r"\bperpendicular vectors?\b",
]

MEDIUM_PATTERNS = [
    r"\bvector\b",
    r"\bvectors\b",
    r"\bperpendicular\b",
    r"\bparallel\b",
    r"\bplane\b",
    r"\bplanes\b",
    r"\bthree dimensions\b",
    r"\b3D\b",
]

def classify_question(text):

    text_lower = text.lower()

    strong_matches = [
        pattern for pattern in STRONG_PATTERNS
        if re.search(pattern, text_lower)
    ]

    medium_matches = [
        pattern for pattern in MEDIUM_PATTERNS
        if re.search(pattern, text_lower)
    ]

    score = len(strong_matches) * 3 + len(medium_matches)

    # Strong evidence is enough to accept a question.
    if strong_matches:
        return "Vector", score, strong_matches

    # Weak evidence goes to manual review, not the final list.
    if score >= 2:
        return "Review", score, medium_matches

    return "Reject", score, []


# ==========================================
# EXTRACT QUESTIONS FROM A PDF
# ==========================================

def extract_questions(pdf_path):

    questions = []

    try:
        with fitz.open(pdf_path) as document:

            current_question = None

            for page_number, page in enumerate(document, 1):

                lines = page.get_text("text").splitlines()

                for line in lines:

                    number, remainder = is_question_start(line)

                    if number is not None:

                        if current_question is not None:
                            questions.append(current_question)

                        current_question = {
                            "Paper": pdf_path.stem,
                            "Question": number,
                            "Start Page": page_number,
                            "End Page": page_number,
                            "Question Text": remainder
                        }

                    elif current_question is not None:

                        if line.strip():
                            current_question["End Page"] = page_number

                        current_question["Question Text"] += (
                            "\n" + line
                        )

            if current_question is not None:
                questions.append(current_question)

    except Exception as error:
        print(f"Error reading {pdf_path.name}: {error}")

    return questions


# ==========================================
# REMOVE DUPLICATES
# ==========================================

def remove_duplicates(questions):

    unique = {}

    for question in questions:

        key = (
            question["Paper"],
            question["Question"],
            question["Start Page"]
        )

        if key not in unique:
            unique[key] = question

    return list(unique.values())


# ==========================================
# CLASSIFY ALL QUESTIONS
# ==========================================

def find_vector_questions(all_questions):

    vector_questions = []
    review_questions = []

    for question in all_questions:

        category, score, evidence = classify_question(
            question["Question Text"]
        )

        question["Detection Score"] = score
        question["Evidence"] = ", ".join(evidence)

        start = question["Start Page"]
        end = question["End Page"]

        question["Pages"] = (
            str(start) if start == end
            else f"{start}-{end}"
        )

        if category == "Vector":
            vector_questions.append(question)

        elif category == "Review":
            review_questions.append(question)

    return vector_questions, review_questions


# ==========================================
# SAVE CSV
# ==========================================

def save_csv(questions, filename):

    output_path = OUTPUT_FOLDER / filename

    fields = [
        "Paper",
        "Question",
        "Pages",
        "Detection Score",
        "Evidence",
        "Question Text"
    ]

    with open(
        output_path,
        "w",
        newline="",
        encoding="utf-8-sig"
    ) as file:

        writer = csv.DictWriter(file, fieldnames=fields)
        writer.writeheader()

        for question in questions:
            writer.writerow({
                field: question.get(field, "")
                for field in fields
            })


# ==========================================
# SAVE JSON
# ==========================================

def save_json(questions):

    output_path = OUTPUT_FOLDER / "vector_questions.json"

    with open(output_path, "w", encoding="utf-8") as file:
        json.dump(
            questions,
            file,
            indent=4,
            ensure_ascii=False
        )


# ==========================================
# SAVE TEXT
# ==========================================

def save_text(questions):

    output_path = OUTPUT_FOLDER / "vector_questions.txt"

    with open(output_path, "w", encoding="utf-8") as file:

        file.write("IB MATH AA HL - VECTOR QUESTIONS\n")
        file.write("=" * 65 + "\n\n")

        for index, question in enumerate(questions, 1):

            file.write(f"QUESTION {index}\n")
            file.write("-" * 65 + "\n")

            file.write(
                f"Paper: {question['Paper']}\n"
            )

            file.write(
                f"Question number: {question['Question']}\n"
            )

            file.write(
                f"Pages: {question['Pages']}\n"
            )

            file.write(
                f"Detection evidence: {question['Evidence']}\n\n"
            )

            file.write(
                question["Question Text"].strip()
            )

            file.write("\n\n")
            file.write("=" * 65 + "\n\n")


# ==========================================
# MAIN
# ==========================================

def main():

    print("\nScanning IB Mathematics AA HL papers...\n")

    pdf_files = sorted(PDF_FOLDER.glob("*.pdf"))

    if not pdf_files:
        print("No PDFs found in IB_Math_Papers.")
        return

    print(f"Found {len(pdf_files)} PDFs.\n")

    all_questions = []

    for index, pdf_path in enumerate(pdf_files, 1):

        print(
            f"[{index}/{len(pdf_files)}] "
            f"{pdf_path.name}"
        )

        all_questions.extend(
            extract_questions(pdf_path)
        )

    all_questions = remove_duplicates(all_questions)

    vector_questions, review_questions = (
        find_vector_questions(all_questions)
    )

    vector_questions.sort(
        key=lambda q: (q["Paper"], q["Start Page"])
    )

    review_questions.sort(
        key=lambda q: (q["Paper"], q["Start Page"])
    )

    save_csv(vector_questions, "vector_questions.csv")
    save_csv(review_questions, "review_required.csv")
    save_json(vector_questions)
    save_text(vector_questions)

    print("\n" + "=" * 50)
    print("SCAN COMPLETE")
    print("=" * 50)

    print(f"PDFs scanned: {len(pdf_files)}")
    print(f"Questions extracted: {len(all_questions)}")
    print(f"Strong vector matches: {len(vector_questions)}")
    print(f"Questions needing review: {len(review_questions)}")

    print("\nFiles created:")
    print("1. vector_questions.csv")
    print("2. vector_questions.json")
    print("3. vector_questions.txt")
    print("4. review_required.csv")

    print(f"\nLocation: {OUTPUT_FOLDER}")


if __name__ == "__main__":
    main()
