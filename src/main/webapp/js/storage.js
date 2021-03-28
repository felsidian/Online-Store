const CartStorage = {

    /*
     * returns cart= {"id" : {name, price, quantity}}
     */
    getCart: function() {
        if(localStorage.cart === undefined) {
            localStorage.cart = JSON.stringify({});
        }
        return JSON.parse(localStorage.cart);
    },

    getTotal: function() {
        const cart = this.getCart();
        let price = 0;
        for (let id of Object.keys(cart)) {
            price += Number.parseFloat(cart[id].price) * Number.parseFloat(cart[id].quantity);
        }
        return price;
    },

    addToCart: function(id, name, price) {
        let cart = this.getCart();
        let quantity = 1;
        if (id in cart)
            quantity += cart[id].quantity;
        cart[id] = {name: name, price: price, quantity: quantity};
        localStorage.cart = JSON.stringify(cart);
    },

    removeFromCart: function(id) {
        let cart = this.getCart();
        delete cart[id];
        localStorage.cart = JSON.stringify(cart);
    },

    setQuantity: function(id, quantity) {
        let cart = this.getCart();
        cart[id].quantity = quantity;
        localStorage.cart = JSON.stringify(cart);
    },

    clearCart: function() {
        localStorage.cart = JSON.stringify({});
    },

}