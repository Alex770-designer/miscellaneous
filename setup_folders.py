
from pathlib import Path

folders = [
    "IB_Math_Papers",
    "Vector_Finder",
    "Vector_Results"
]

for folder in folders:
    Path(folder).mkdir(exist_ok=True)

print("Project folders created successfully!")
