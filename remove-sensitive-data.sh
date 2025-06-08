#!/bin/bash

# Create a new branch without the history
git checkout --orphan temp_branch

# Add all files
git add .

# Commit the changes
git commit -m "Initial commit with sensitive data removed"

# Delete the main branch
git branch -D main

# Rename the current branch to main
git branch -m main

# Force push to remote
git push -f origin main

# Clean up
git gc --aggressive --prune=all 