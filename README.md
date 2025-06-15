# TaskForge 🛠️

TaskForge is a Java-based smart task prioritization simulator that ranks and selects the best tasks based on multiple real-world attributes like priority, deadline urgency, risk, and cost.

## Features
- Random task generation (10 tasks/run)
- Scoring using a weighted model
- Tie-breaker logic for fair ranking
- Console output and `.txt` report file
- One-file Java project, framework-free

## Sample Output (Console)
1. T03 → Score: 0.879 | Priority: 5 | Deadline: 12h | Risk: Low | Cost: $450  
...

## Technologies Used
- Java 21
- FileWriter
- List sorting with custom comparator
