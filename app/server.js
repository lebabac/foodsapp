const express = require('express');
const app = express();
const port = 3000;

// Middleware để phân tích dữ liệu JSON từ yêu cầu
app.use(express.json());

// Xử lý yêu cầu POST đến '/addFood'
app.post('/addFood', (req, res) => {
    const foodData = req.body;
    // Ở đây, bạn có thể thêm mã để xử lý dữ liệu foodData, ví dụ: lưu vào cơ sở dữ liệu.
    console.log('Dữ liệu món ăn nhận được:', foodData);
    res.send('Món ăn đã được thêm thành công!');
});

// Lắng nghe yêu cầu trên cổng 3000
app.listen(port, () => {
    console.log(`Máy chủ đang lắng nghe trên http://localhost:${port}`);
});
