const cartButton = document.getElementById('cart-button');
const cartSizeEl = document.getElementById('cart-size');
const totalPriceEl = document.getElementById('total-price');
const cartBody = document.getElementById('cart-list');
const orderForm = document.getElementById('order-form');
const submitOrderButton = document.getElementById('order-sobmit');

orderForm.addEventListener('submit', event => {
    const cart = CartStorage.getCart();
    let requestParams = "";
    for (let id of Object.keys(cart)) {
        requestParams = requestParams + id + ",";
    }
    orderForm["pids"].value = requestParams;
    CartStorage.clearCart();
    if(requestParams === "")
        event.preventDefault();

});

cartButton.addEventListener('click', () => {
    fillCart();
});

function onPageLoad() {
    updateCartSizeEl();
}


// for all "Add to cart" buttons
document.querySelectorAll('.add-to-cart').forEach(item => {
    item.addEventListener('click', event => {
        // delete position from cart
        let productStrings = item.getAttribute("data-pr-info").split(';');
        CartStorage.addToCart(productStrings[0], productStrings[1], productStrings[2]);
        updateCartSizeEl();
    })
});

function updateCartSizeEl() {
    let cartSize = Object.keys(CartStorage.getCart()).length;
    let str = "";
    if(cartSize > 0) {
        str = cartSize.toString();
        submitOrderButton.removeAttribute("disabled");
    }
    else
        submitOrderButton.setAttribute("disabled", "");
    cartSizeEl.innerText = str;
}

function updateTotal() {
    totalPriceEl.innerText = CartStorage.getTotal().toString();
}

function fillCart() {

    updateTotal();
    cartBody.innerHTML = '';

    const cart = CartStorage.getCart();
    for (let id of Object.keys(cart)) {
        const product = cart[id];
        let newElement = document.createElement("li");   //create a div
        newElement.setAttribute("class", "list-group-item");
        newElement.innerHTML = createCartItemElement(id, product.name, product.price, product.quantity);
        cartBody.append(newElement);
    }

    // for all delete button in the cart
    document.querySelectorAll('.delete-from-cart').forEach(item => {
        item.addEventListener('click', () => {
            // delete position from cart
            CartStorage.removeFromCart(item.getAttribute("value"));
            updateCartSizeEl();
            fillCart();
        })
    });

    // for all quantity fields in the cart
    document.querySelectorAll('.quantity-input').forEach(item => {
        item.addEventListener('input', () => {
            // delete position from cart
            let quantity = Number.parseInt(item.value);

            if(!isNaN(quantity) && quantity > 0 && quantity < 999) {
                CartStorage.setQuantity(item.getAttribute("data-id"), quantity);
                console.log(quantity);
            }
            updateTotal();
        })
    });
}

function createCartItemElement(id, name, price, quantity) {
    return [
        '<div class="row d-flex align-items-center">',
        '<div class="col-3">',
        '<img class="card-img-top p-3" src="https://i2.rozetka.ua/goods/10128204/bosch_06039a210b_images_10128204791.jpg" style="height: 100px; object-fit: scale-down;" alt="">',
        '</div>',
        '<div class="col-8">',
        '<div class="row d-flex align-items-center">',
        '<div class="col-7">',
        '<div class="product-name">',
        '<a href="product?id=' + id + '">' + name + '</a>',
        '</div>',
        '</div>',
        '<div class="col-2">',
        '<input type="number" min="1" max="999" value="' + quantity + '" data-id="' + id + '" class="form-control quantity-input">',
        '</div>',
        '<div class="col-2">',
        '<span>' + price + '</span>',
        '</div>',
        '<div class="col-1 ">',
        '<button type="button" class="btn-close float-end delete-from-cart" aria-label="Close" value="' + id + '"></button>',
        '</div>',
        '</div>',
        '</div>',
        '</div>'
    ].join('\n');
}



