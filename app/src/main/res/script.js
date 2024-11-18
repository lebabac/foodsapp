// Function to handle form submission (add/edit food item)
function handleFormSubmission(event) {
    event.preventDefault();
    console.log('Biểu mẫu được gửi đi!'); // Thêm dòng này để kiểm tra

    // Extract form data
    const formData = new FormData(event.target);
    const foodName = formData.get('foodName');
    const foodPrice = formData.get('foodPrice');
    const foodIngredients = formData.get('foodIngredients');
    const foodOrigin = formData.get('foodOrigin');
    const foodImage = formData.get('foodImage'); // This needs further processing if needed

    // Perform validation (you can add more validation logic here)

    // If valid, add/edit food item in SQLite database
    if (foodName && foodPrice && foodIngredients && foodOrigin) {
        const foodItem = {
            name: foodName,
            price: parseFloat(foodPrice),
            ingredients: foodIngredients,
            origin: foodOrigin,
            image: foodImage // This might need further processing or uploading to server
        };

        // Send data to server
        fetch('http://localhost:3000/addFood', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(foodItem)
        })
        .then(response => {
            if (response.ok) {
                console.log('Thêm món ăn thành công!');
                // Clear form fields
                event.target.reset();
                // Refresh the food items list
                displayFoodItems();
            } else {
                console.error('Có lỗi xảy ra khi thêm món ăn:', response.statusText);
            }
        })
        .catch(error => {
            console.error('Có lỗi xảy ra khi gửi dữ liệu:', error);
        });
    }
}

// Attach event listener to form submission
document.getElementById('foodForm').addEventListener('submit', handleFormSubmission);
