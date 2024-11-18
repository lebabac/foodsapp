package com.example.food_ordering_app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val mContext: Context = context
    companion object {

        private const val DATABASE_NAME = "ListFoods.db"
        private const val DATABASE_VERSION = 8

        // Bảng User
        private const val TABLE_USER = "user"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_ROLE = "role"

        // Bảng Food
        private const val TABLE_FOOD = "food"
        private const val COLUMN_FOOD_ID = "id"
        private const val COLUMN_FOOD_NAME = "name"
        private const val COLUMN_FOOD_DETAILS = "details"
        private const val COLUMN_FOOD_PRICE = "price"
        private const val COLUMN_FOOD_LOCATION = "location"
        private const val COLUMN_FOOD_IMAGE = "image"

        // Bảng Orders
        private const val TABLE_ORDERS = "orders"
        private const val COLUMN_ORDER_ID = "order_id"
        private const val COLUMN_FULL_NAME = "full_name"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_ORDER_ADDRESS = "order_address"
        private const val COLUMN_QUANTITY = "order_quantity"
        private const val COLUMN_PAYMENT_METHOD = "payment_method"
        private const val COLUMN_TOTAL_PRICE = "total_price"
        private const val COLUMN_ORDER_USER_ID = "user_id"
        private const val COLUMN_ORDER_FOOD_ID = "food_id"
        private const val COLUMN_STATUS = "status" // Khai báo cột status



        // Bảng Cart
        private const val TABLE_CART = "cart"
        private const val COLUMN_CART_ID = "id"
        private const val COLUMN_CART_FOOD_ID = "food_id"
        private const val COLUMN_CART_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableUser = ("CREATE TABLE $TABLE_USER ("
                + "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_USERNAME TEXT, "
                + "$COLUMN_PHONE TEXT, "
                + "$COLUMN_EMAIL TEXT, "
                + "$COLUMN_ADDRESS TEXT, "
                + "$COLUMN_PASSWORD TEXT, "
                + "$COLUMN_ROLE TEXT NOT NULL CHECK($COLUMN_ROLE IN ('admin', 'user')))")
        db.execSQL(createTableUser)

        val createTableFood = ("CREATE TABLE $TABLE_FOOD ("
                + "$COLUMN_FOOD_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_FOOD_NAME TEXT, "
                + "$COLUMN_FOOD_DETAILS TEXT, "
                + "$COLUMN_FOOD_PRICE REAL, "
                + "$COLUMN_FOOD_LOCATION TEXT, "
                + "$COLUMN_FOOD_IMAGE BLOB)")
        db.execSQL(createTableFood)

        val createTableOrder = ("CREATE TABLE $TABLE_ORDERS ("
                + "$COLUMN_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_FULL_NAME TEXT, "
                + "$COLUMN_PHONE_NUMBER TEXT, "
                + "$COLUMN_ORDER_ADDRESS TEXT, "
                + "$COLUMN_QUANTITY INTEGER, "
                + "$COLUMN_PAYMENT_METHOD TEXT, "
                + "$COLUMN_TOTAL_PRICE REAL, "
                + "$COLUMN_ORDER_USER_ID INTEGER, "
                + "$COLUMN_ORDER_FOOD_ID INTEGER, "
                + "$COLUMN_STATUS TEXT DEFAULT 'pending', " // Thêm cột status
                + "FOREIGN KEY($COLUMN_ORDER_USER_ID) REFERENCES $TABLE_USER($COLUMN_USER_ID), "
                + "FOREIGN KEY($COLUMN_ORDER_FOOD_ID) REFERENCES $TABLE_FOOD($COLUMN_FOOD_ID))")
        db.execSQL(createTableOrder)

        val createTableCart = ("CREATE TABLE $TABLE_CART ("
                + "$COLUMN_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_CART_FOOD_ID INTEGER, "
                + "$COLUMN_CART_QUANTITY INTEGER)")
        db.execSQL(createTableCart)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 7) { // Thực hiện logic nâng cấp nếu phiên bản cũ nhỏ hơn 4
            db.execSQL("ALTER TABLE $TABLE_ORDERS ADD COLUMN $COLUMN_STATUS TEXT DEFAULT 'pending'")
        }
    }



    fun addUser(user: User): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PHONE, user.phone)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_ADDRESS, user.address)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_ROLE, user.role)
        }
        val result = db.insert(TABLE_USER, null, contentValues)
        db.close()
        return result
    }

    fun addFood(food: Food): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_FOOD_NAME, food.name)
            put(COLUMN_FOOD_DETAILS, food.details)
            put(COLUMN_FOOD_PRICE, food.price)
            put(COLUMN_FOOD_LOCATION, food.location)
            put(COLUMN_FOOD_IMAGE, food.image)
        }
        val result = db.insert(TABLE_FOOD, null, contentValues)
        db.close()
        return result
    }

    fun updateFood(food: Food): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_FOOD_NAME, food.name)
            put(COLUMN_FOOD_DETAILS, food.details)
            put(COLUMN_FOOD_PRICE, food.price)
            put(COLUMN_FOOD_LOCATION, food.location)
            put(COLUMN_FOOD_IMAGE, food.image)
        }
        val result = db.update(TABLE_FOOD, contentValues, "$COLUMN_FOOD_ID = ?", arrayOf(food.id.toString()))
        db.close()
        return result
    }

    fun deleteFood(foodId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_FOOD, "$COLUMN_FOOD_ID = ?", arrayOf(foodId.toString()))
        db.close()
        return result
    }

    fun checkAdmin(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ? AND $COLUMN_ROLE = 'admin'"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }
    fun getUserId(username: String, password: String): Long {
        val db = this.readableDatabase
        var userId: Long = -1

        val query = "SELECT $COLUMN_USER_ID FROM $TABLE_USER WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))

        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
        }

        cursor.close()
        db.close()
        return userId
    }
    fun checkUsername(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    fun getAllFood(): List<Food> {
        val foodList = mutableListOf<Food>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_FOOD", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME))
                val details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_DETAILS))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PRICE))
                val location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_LOCATION))
                val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FOOD_IMAGE))
                foodList.add(Food(id, name, details, price, location, image))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return foodList
    }

    fun addOrder(order: Order): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_FULL_NAME, order.fullName)
            put(COLUMN_PHONE_NUMBER, order.phoneNumber)
            put(COLUMN_ORDER_ADDRESS, order.address)
            put(COLUMN_QUANTITY, order.quantity)
            put(COLUMN_PAYMENT_METHOD, order.paymentMethod)
            put(COLUMN_TOTAL_PRICE, order.totalPrice)
            put(COLUMN_ORDER_USER_ID, order.userId)
            put(COLUMN_ORDER_FOOD_ID, order.foodId)
            put(COLUMN_STATUS, order.status) // Thêm status
        }
        val result = db.insert(TABLE_ORDERS, null, contentValues)
        db.close()
        return result
    }



    fun updateOrder(order: Order): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_FULL_NAME, order.fullName)
            put(COLUMN_PHONE_NUMBER, order.phoneNumber)
            put(COLUMN_ORDER_ADDRESS, order.address)
            put(COLUMN_QUANTITY, order.quantity)
            put(COLUMN_PAYMENT_METHOD, order.paymentMethod)
            put(COLUMN_TOTAL_PRICE, order.totalPrice)
            put(COLUMN_ORDER_USER_ID, order.userId)
            put(COLUMN_ORDER_FOOD_ID, order.foodId)

        }
        val result = db.update(TABLE_ORDERS, contentValues, "$COLUMN_ORDER_ID = ?", arrayOf(order.id.toString()))
        db.close()
        return result
    }

    fun deleteOrder(orderId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_ORDERS, "$COLUMN_ORDER_ID = ?", arrayOf(orderId.toString()))
        db.close()
        return result
    }
    fun getPendingOrders(): List<Order> {
        val orderList = mutableListOf<Order>()
        val db = this.readableDatabase
        val query = """
        SELECT o.*, f.name AS foodName, f.price AS foodPrice, f.image AS foodImage
        FROM $TABLE_ORDERS o
        INNER JOIN $TABLE_FOOD f ON o.food_id = f.id
        WHERE o.status = 'pending'
    """
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"))
                val fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("order_address"))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("order_quantity"))
                val paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"))
                val totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"))
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
                val foodId = cursor.getInt(cursor.getColumnIndexOrThrow("food_id"))
                val foodName = cursor.getString(cursor.getColumnIndexOrThrow("foodName"))
                val foodPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("foodPrice"))
                val foodImage = cursor.getBlob(cursor.getColumnIndexOrThrow("foodImage"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))

                val order = Order(id, fullName, phoneNumber, address, quantity, paymentMethod, totalPrice, userId, foodId, status)
                order.foodName = foodName
                order.foodPrice = foodPrice
                order.foodImage = foodImage

                orderList.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return orderList
    }
    fun getOrdersByStatusAndUserId(status: String, userId: Long): List<Order> {
        val orderList = mutableListOf<Order>()
        val db = this.readableDatabase
        val query = """
        SELECT o.*, f.name AS foodName, f.price AS foodPrice, f.image AS foodImage, o.status AS status
        FROM $TABLE_ORDERS o 
        INNER JOIN $TABLE_FOOD f ON o.food_id = f.id
        WHERE o.status = ? AND o.user_id = ?
    """
        val cursor = db.rawQuery(query, arrayOf(status, userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"))
                val fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("order_address"))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("order_quantity"))
                val paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"))
                val totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"))
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
                val foodId = cursor.getInt(cursor.getColumnIndexOrThrow("food_id"))

                val foodName = cursor.getString(cursor.getColumnIndexOrThrow("foodName"))
                val foodPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("foodPrice"))
                val foodImage = cursor.getBlob(cursor.getColumnIndexOrThrow("foodImage"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))

                val order = Order(id, fullName, phoneNumber, address, quantity, paymentMethod, totalPrice, userId, foodId, status)
                order.foodName = foodName
                order.foodPrice = foodPrice
                order.foodImage = foodImage

                orderList.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return orderList
    }


    fun getOrderCountByStatusAndUserId(status: String, userId: Long): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_ORDERS WHERE status = ? AND user_id = ?"
        val cursor = db.rawQuery(query, arrayOf(status, userId.toString()))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }

    fun updateOrderStatus(orderId: Int, status: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("status", status)

        val result = db.update(TABLE_ORDERS, contentValues, "order_id = ?", arrayOf(orderId.toString()))
        db.close()
        return result > 0
    }
    fun getApprovedOrders(): List<Order> {
        val orderList = mutableListOf<Order>()
        val db = this.readableDatabase
        val query = """
        SELECT o.*, f.name AS foodName, f.price AS foodPrice, f.image AS foodImage 
        FROM $TABLE_ORDERS o 
        INNER JOIN $TABLE_FOOD f ON o.food_id = f.id
        WHERE o.status = 'approved'
    """
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"))
                val fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("order_address"))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("order_quantity"))
                val paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"))
                val totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"))
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
                val foodId = cursor.getInt(cursor.getColumnIndexOrThrow("food_id"))

                val foodName = cursor.getString(cursor.getColumnIndexOrThrow("foodName"))
                val foodPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("foodPrice"))
                val foodImage = cursor.getBlob(cursor.getColumnIndexOrThrow("foodImage"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))

                val order = Order(id, fullName, phoneNumber, address, quantity, paymentMethod, totalPrice, userId, foodId, status)
                order.foodName = foodName
                order.foodPrice = foodPrice
                order.foodImage = foodImage

                orderList.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return orderList
    }
    fun getAllOrders(): List<Order> {
        val orderList = mutableListOf<Order>()
        val db = this.readableDatabase
        val query = """
           SELECT o.*, f.name AS foodName, f.price AS foodPrice, f.image AS foodImage, o.status AS status
        FROM $TABLE_ORDERS o 
        INNER JOIN $TABLE_FOOD f ON o.food_id = f.id
         WHERE o.status != 'approved'
    """
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"))
                val fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("order_address"))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("order_quantity"))
                val paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"))
                val totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"))
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
                val foodId = cursor.getInt(cursor.getColumnIndexOrThrow("food_id"))

                val foodName = cursor.getString(cursor.getColumnIndexOrThrow("foodName"))
                val foodPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("foodPrice"))
                val foodImage = cursor.getBlob(cursor.getColumnIndexOrThrow("foodImage"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))

                val order = Order(id, fullName, phoneNumber, address, quantity, paymentMethod, totalPrice, userId, foodId,status)
                // Lưu các thông tin food vào đối tượng order
                order.foodName = foodName
                order.foodPrice = foodPrice
                order.foodImage = foodImage

                orderList.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return orderList
    }


    fun getFoodPrice(foodId: Int): Double {
        val db = this.readableDatabase
        var price = 0.0
        val query = "SELECT $COLUMN_FOOD_PRICE FROM $TABLE_FOOD WHERE $COLUMN_FOOD_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(foodId.toString()))

        if (cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PRICE))
        }

        cursor.close()
        db.close()

        return price
    }


    fun getFoodById(foodId: Int): Food? {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        var food: Food? = null

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_FOOD WHERE $COLUMN_FOOD_ID = ?", arrayOf(foodId.toString()))
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME))
                val details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_DETAILS))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PRICE))
                val location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_LOCATION))
                val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FOOD_IMAGE))
                food = Food(id, name, details, price, location, image)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return food
    }

    fun searchFoodByName(keyword: String): List<Food> {
        val foodList = mutableListOf<Food>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_FOOD WHERE $COLUMN_FOOD_NAME LIKE ?", arrayOf("%$keyword%"))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME))
                val details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_DETAILS))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PRICE))
                val location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_LOCATION))
                val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FOOD_IMAGE))
                foodList.add(Food(id, name, details, price, location, image))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return foodList
    }

    fun addToCart(foodId: Int, quantity: Int): Long {
        val db = this.writableDatabase

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        val cursor = db.rawQuery("SELECT $COLUMN_CART_ID, $COLUMN_CART_QUANTITY FROM $TABLE_CART WHERE $COLUMN_CART_FOOD_ID = ?", arrayOf(foodId.toString()))
        return if (cursor.moveToFirst()) {
            val existingQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY))
            val newQuantity = existingQuantity + quantity

            val contentValues = ContentValues().apply {
                put(COLUMN_CART_QUANTITY, newQuantity)
            }

            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID))
            db.update(TABLE_CART, contentValues, "$COLUMN_CART_ID = ?", arrayOf(id.toString())).toLong()
        } else {
            val contentValues = ContentValues().apply {
                put(COLUMN_CART_FOOD_ID, foodId)
                put(COLUMN_CART_QUANTITY, quantity)
            }
            db.insert(TABLE_CART, null, contentValues)
        }.also {
            cursor.close()
            db.close()
        }
    }
    fun updateCartItem(cartItemId: Int, quantity: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CART_QUANTITY, quantity)
        }
        val result = db.update(TABLE_CART, contentValues, "$COLUMN_CART_ID = ?", arrayOf(cartItemId.toString()))
        db.close()
        return result
    }
    fun deleteCartItem(cartItemId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CART, "$COLUMN_CART_ID = ?", arrayOf(cartItemId.toString()))
        db.close()
        return result
    }
    fun updateCartItemQuantity(cartItemId: Int, newQuantity: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CART_QUANTITY, newQuantity)
        }
        val result = db.update(TABLE_CART, contentValues, "$COLUMN_CART_ID = ?", arrayOf(cartItemId.toString()))
        db.close()
        return result
    }
    fun createOrder(cartItems: List<CartItem>, fullName: String, phoneNumber: String, address: String, paymentMethod: String, totalOrderPrice: Double, userId: Int, status: String): Long {
        val db = this.writableDatabase

        // Tạo nội dung cho bảng orders
        val contentValues = ContentValues().apply {
            put(COLUMN_FULL_NAME, fullName)
            put(COLUMN_PHONE_NUMBER, phoneNumber)
            put(COLUMN_ORDER_ADDRESS, address)
            put(COLUMN_PAYMENT_METHOD, paymentMethod)
            put(COLUMN_TOTAL_PRICE, totalOrderPrice)
            put(COLUMN_USER_ID, userId)
            put(COLUMN_STATUS, status)
        }

        // Thêm vào bảng orders
        val orderId = db.insert(TABLE_ORDERS, null, contentValues)

        // Thêm từng món hàng trong giỏ hàng vào bảng order_items
        cartItems.forEach {
            val orderItemContentValues = ContentValues().apply {
                put(COLUMN_ORDER_ID, orderId)
                put(COLUMN_FOOD_ID, it.foodId)
                put(COLUMN_QUANTITY, it.quantity)
                put(COLUMN_TOTAL_PRICE, totalOrderPrice)
            }
            db.insert(TABLE_ORDERS, null, orderItemContentValues)
        }

        db.close()
        return orderId
    }

    fun getAllCartItems(): List<CartItem> {
        val cartItemList = mutableListOf<CartItem>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_CART"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val cartItemId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID))
                val foodId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_FOOD_ID))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY))
                cartItemList.add(CartItem(cartItemId, foodId, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return cartItemList
    }

    // Phương thức để xóa các món trong giỏ hàng sau khi đã tạo đơn hàng thành công
    fun deleteCartItems(cartItemIds: List<Int>): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CART, "$COLUMN_CART_ID IN (${cartItemIds.joinToString()})", null)
        db.close()
        return result
    }


    fun updateUser(user: User): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PHONE, user.phone)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_ADDRESS, user.address)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_ROLE, user.role)
        }
        val result = db.update(TABLE_USER, contentValues, "$COLUMN_USER_ID = ?", arrayOf(user.id.toString()))
        db.close()
        return result
    }
    fun getCurrentUserFromDatabase(userId: Long): User? {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        var user: User? = null

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_USER WHERE $COLUMN_USER_ID = ?", arrayOf(userId.toString()))
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
                val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
                val role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
                user = User(id, username, phone, email, address, password, role)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return user
    }





}
