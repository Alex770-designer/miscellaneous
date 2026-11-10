
import time
from pathlib import Path

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.common.exceptions import (
    WebDriverException,
    TimeoutException
)


# ==========================================
# CONFIGURATION
# ==========================================

WEBSITE_URL = (
    "https://ibdocs.re/past-papers-by-subject/"
    "mathematics-analysis-and-approaches"
)

DOWNLOAD_FOLDER = Path("IB_Math_Papers").resolve()
DOWNLOAD_FOLDER.mkdir(exist_ok=True)

document_queue = []


# ==========================================
# BROWSER SETUP
# ==========================================

def setup_browser():

    options = Options()

    options.add_experimental_option(
        "prefs",
        {
            "download.default_directory": str(DOWNLOAD_FOLDER),
            "download.prompt_for_download": False,
            "download.directory_upgrade": True,
            "plugins.always_open_pdf_externally": True
        }
    )

    driver = webdriver.Chrome(options=options)

    return driver


# ==========================================
# FIND DOCUMENT PAGES
# ==========================================

def get_documents(driver):

    driver.get(WEBSITE_URL)
    time.sleep(4)

    links = driver.find_elements(
        By.CSS_SELECTOR,
        "a[href*='/documents/']"
    )

    documents = []
    seen = set()

    for link in links:

        url = link.get_attribute("href")
        title = link.text.strip()

        if url and url not in seen:

            seen.add(url)

            documents.append({
                "title": title or "Untitled document",
                "url": url
            })

    return documents


# ==========================================
# FIND VISIBLE DOWNLOAD BUTTONS
# ==========================================

def find_download_buttons(driver):

    elements = driver.find_elements(
        By.XPATH,
        "//a | //button | //*[@role='button']"
    )

    buttons = []

    for element in elements:

        label = " ".join([
            element.text or "",
            element.get_attribute("aria-label") or "",
            element.get_attribute("title") or ""
        ]).lower()

        if (
            "download" in label
            and element.is_displayed()
            and element.is_enabled()
        ):

            buttons.append(element)

    return buttons


# ==========================================
# WAIT FOR DOWNLOAD
# ==========================================

def wait_for_download(previous_files, timeout=90):

    start = time.time()

    while time.time() - start < timeout:

        current_files = set(DOWNLOAD_FOLDER.iterdir())

        partial_files = [
            f for f in current_files
            if f.name.endswith(".crdownload")
        ]

        new_files = [
            f for f in current_files - previous_files
            if f.is_file()
            and f.suffix.lower() == ".pdf"
        ]

        if new_files and not partial_files:

            return max(
                new_files,
                key=lambda f: f.stat().st_mtime
            )

        time.sleep(1)

    return None


# ==========================================
# FIND AND CLICK DOWNLOAD
# ==========================================

def download_from_document_page(driver, document):

    original_tab = driver.current_window_handle

    try:

        # Open individual document page in new tab
        driver.switch_to.new_window("tab")

        driver.get(document["url"])

        time.sleep(3)

        # Record existing files before downloading
        previous_files = set(DOWNLOAD_FOLDER.iterdir())

        wait = WebDriverWait(driver, 20)

        # Wait for at least one Download button
        wait.until(
            lambda d: len(find_download_buttons(d)) >= 1
        )

        download_buttons = find_download_buttons(driver)

        print(
            f"Found {len(download_buttons)} "
            f"Download button(s)."
        )

        # CASE 1: Both buttons are already visible
        if len(download_buttons) >= 2:

            print("Clicking the second Download button...")

            download_buttons[1].click()

        # CASE 2: First button must be clicked to reveal second
        else:

            print("Clicking the first Download button...")

            download_buttons[0].click()

            time.sleep(2)

            try:

                wait.until(
                    lambda d: len(find_download_buttons(d)) >= 2
                )

                download_buttons = find_download_buttons(driver)

                print("Second Download button appeared.")

                download_buttons[1].click()

                print("Clicked the second Download button.")

            except TimeoutException:

                print(
                    "No second button appeared. "
                    "Checking whether the first click started the download."
                )

        # Verify actual download
        downloaded_file = wait_for_download(
            previous_files
        )

        if downloaded_file:

            print("Downloaded:", downloaded_file.name)

            document_queue.append({
                "title": document["title"],
                "path": str(downloaded_file),
                "status": "downloaded"
            })

            return downloaded_file

        print("Download timed out:", document["title"])

        return None

    except WebDriverException as error:

        print("Browser error:", error)

        return None

    finally:

        # Close only the document tab
        if original_tab in driver.window_handles:

            if driver.current_window_handle != original_tab:
                driver.close()

            driver.switch_to.window(original_tab)


# ==========================================
# MAIN PROGRAM
# ==========================================

def main():

    driver = setup_browser()

    try:

        print("Loading IB Mathematics AA documents...")

        documents = get_documents(driver)

        print(f"\nFound {len(documents)} document pages.\n")

        for index, document in enumerate(documents):

            print(
                f"[{index}] {document['title']}"
            )

        print("\nEnter document numbers separated by commas.")
        print("Example: 0, 5, 12, 20")

        selection = input("\nYour selection: ")

        selected_indices = []

        for value in selection.split(","):

            value = value.strip()

            if value.isdigit():

                index = int(value)

                if 0 <= index < len(documents):
                    selected_indices.append(index)

        # Remove duplicate selections
        selected_indices = list(dict.fromkeys(selected_indices))

        selected_documents = [
            documents[i] for i in selected_indices
        ]

        print(
            f"\nStarting downloads: "
            f"{len(selected_documents)} documents\n"
        )

        for document in selected_documents:

            print("\nProcessing:", document["title"])

            download_from_document_page(
                driver,
                document
            )

            # Avoid overwhelming the website
            time.sleep(2)

        print("\n========== COMPLETE ==========")

        print("Successfully downloaded:", len(document_queue))

        print("\nFiles are stored in:")
        print(DOWNLOAD_FOLDER)

        print("\nDocuments added to processing queue:")

        for item in document_queue:
            print(item["path"])

    finally:

        driver.quit()


if __name__ == "__main__":
    main()
