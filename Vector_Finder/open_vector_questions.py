
from pathlib import Path
import fitz
import json
import os

PROJECT_FOLDER = Path(__file__).resolve().parent.parent
PDF_FOLDER = PROJECT_FOLDER / "IB_Math_Papers"
RESULTS_FOLDER = PROJECT_FOLDER / "Vector_Results"
JSON_FILE = RESULTS_FOLDER / "vector_questions.json"
OUTPUT_FILE = RESULTS_FOLDER / "Vector_Questions_Visual.pdf"

# Load question data
with open(JSON_FILE, "r", encoding="utf-8") as file:
    questions = json.load(file)

# Remove duplicate entries using paper + question number
unique_questions = {}

for question in questions:
    key = (question["Paper"], question["Question"])

    if key not in unique_questions:
        unique_questions[key] = question

questions = list(unique_questions.values())

# Group questions by paper
papers = {}

for question in questions:
    papers.setdefault(question["Paper"], []).append(question)

# Sort questions by their starting page
for paper in papers:
    papers[paper].sort(
        key=lambda q: int(q["Start Page"])
    )

# Find original PDFs
pdf_files = {
    path.stem: path
    for path in PDF_FOLDER.glob("*.pdf")
}

combined_pdf = fitz.open()
table_of_contents = []

successful = 0
missing = 0

for paper, paper_questions in papers.items():

    pdf_path = pdf_files.get(paper)

    if pdf_path is None:
        print(f"Missing PDF: {paper}")
        missing += 1
        continue

    # Get the unique starting pages in this paper
    start_pages = sorted(set(
        int(q["Start Page"]) for q in paper_questions
    ))

    with fitz.open(pdf_path) as source_pdf:

        for question in paper_questions:

            question_number = question["Question"]
            start_page = int(question["Start Page"])
            end_page = int(question["End Page"])

            # Find the next question that starts on a later page
            later_pages = [
                page for page in start_pages
                if page > start_page
            ]

            if later_pages:
                next_start = min(later_pages)

                # Do not include the next question's starting page
                end_page = min(end_page, next_start - 1)

            # Safety checks
            start_index = start_page - 1
            end_index = end_page - 1

            if start_index < 0 or end_index >= len(source_pdf):
                print(
                    f"Invalid pages: {paper}, Q{question_number}"
                )
                continue

            if end_index < start_index:
                end_index = start_index

            destination_page = combined_pdf.page_count + 1

            combined_pdf.insert_pdf(
                source_pdf,
                from_page=start_index,
                to_page=end_index
            )

            bookmark = f"{paper} | Q{question_number}"

            table_of_contents.append([
                1,
                bookmark,
                destination_page
            ])

            successful += 1

            print(
                f"Added Q{question_number}: "
                f"PDF pages {start_page}-{end_page}"
            )

# Save the combined PDF
if successful == 0:
    print("No questions were successfully added.")
    raise SystemExit

combined_pdf.set_toc(table_of_contents)

combined_pdf.set_metadata({
    "title": "IB Mathematics AA HL Vector Questions",
    "author": "Vector Question Finder",
    "subject": "Collected original IB mathematics questions"
})

combined_pdf.save(
    OUTPUT_FILE,
    garbage=4,
    deflate=True
)

combined_pdf.close()

print("\n================================")
print("YOUR UPDATED PDF IS READY")
print("================================")
print(f"Questions added: {successful}")
print(f"Missing PDFs: {missing}")
print(f"Saved to: {OUTPUT_FILE}")

os.startfile(OUTPUT_FILE)
