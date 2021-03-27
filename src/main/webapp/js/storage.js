class CartStorage {

    /*
     * returns cart= {"id" : {name, price, quantity}}
     */
    static getCart() {
        return JSON.parse(localStorage.cart);
    }

    static getTotal() {
        const cart = JSON.parse(localStorage.cart);
        let price = 0;
        for (let id of Object.keys(cart)) {
            price += Number.parseFloat(cart[id].price) * Number.parseFloat(cart[id].quantity);
        }
        return price;
    }

    static addToCart(id, name, price) {
        let cart = JSON.parse(localStorage.cart);
        let quantity = 1;
        if (id in cart)
            quantity += cart[id].quantity;
        cart[id] = {name: name, price: price, quantity: quantity};
        localStorage.cart = JSON.stringify(cart);
    }

    static removeFromCart(id) {
        let cart = JSON.parse(localStorage.cart);
        delete cart[id];
        localStorage.cart = JSON.stringify(cart);
    }

    static setQuantity(id, quantity) {
        let cart = JSON.parse(localStorage.cart);
        cart[id].quantity = quantity;
        localStorage.cart = JSON.stringify(cart);
    }

    static clearCart() {
        localStorage.cart = JSON.stringify({});
    }


}