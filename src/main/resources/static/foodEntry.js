// Get form and food log elements
const foodEntryForm = document.getElementById("foodEntryForm");
const foodLog = document.getElementById("foodLog");

// Handle form submission
foodEntryForm.addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent form from refreshing the page

    // Get form data
    const dateTime = document.getElementById("datetime").value;
    const foodName = document.getElementById("foodName").value;
    const calories = document.getElementById("calories").value;

    // Create a new food entry
    const foodEntry = document.createElement("li");
    foodEntry.textContent = `${dateTime} - ${foodName} - ${calories} kcal`;

    // Add entry to the food log
    foodLog.appendChild(foodEntry);

    // Clear the form
    foodEntryForm.reset();
});
