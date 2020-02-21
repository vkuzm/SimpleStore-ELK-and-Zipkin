const cookieName = "cart";
const cookieDuration = 7;

window.onload = function () {
  const pageId = document.body.id;
  switch (pageId) {
    case "index" :
      getProducts();
      break;
    case "checkout" :
      getProductsInCart();
      break;
  }
};

function getProducts() {
  const client = new HttpClient();
  client.get('http://localhost:8081/api/products', function (response) {
    console.log(response);

    response.forEach(function (product) {
      generateProduct(product);
    });
  });
}

function getProductsInCart() {
  const client = new HttpClient();
  client.get('http://localhost:8082/api/checkout', function (response) {
    console.log(response);

    //response.forEach(function (product) {
    //  generateProduct(product);
    //});
  });
}

const generateProduct = function (product) {
  const html = '<div class="card mb-4 shadow-sm">'
      + '<div class="card-header"><h4 class="my-0 font-weight-normal">'
      + product.name + '</h4></div>'
      + '<div class="card-body">'
      + '<h1 class="card-title pricing-card-title">$' + product.price + '</h1>'
      + '<p>' + product.description + '</p>'
      + '<button type="button" onclick="addToCart(' + product.id
      + ')" class="btn btn-lg btn-block btn-primary">Buy</button>'
      + '</div>'
      + '</div>';

  document.getElementById("products").insertAdjacentHTML('beforeend', html);
};

const addToCart = function (productId) {
  let isProductExisted = false;
  const productsInCart = this.getCartProduct();

  if (hasProducts()) {
    updateCart();
  }

  if (!isProductExisted) {
    addToCart();
  }

  function hasProducts() {
    return productsInCart.length > 0;
  }

  function updateCart() {
    productsInCart.forEach(function (product) {
      if (product.productId === productId) {
        product.quantity++;
        isProductExisted = true;
        return true;
      }
    });
    return false;
  }

  function addToCart() {
    productsInCart.push({
      productId: productId,
      quantity: 1
    });
  }

  setCookie(cookieName, JSON.stringify(productsInCart), cookieDuration);
  console.log("Product" + productId + " is added to the cart!");
};

function getCartProduct() {
  let products = [];

  let getCartProducts = this.getCookie("cart");
  if (getCartProducts.length > 0) {
    products = JSON.parse(getCartProducts);
  }
  return products;
}

const HttpClient = function () {
  this.get = function (url, callback) {
    const req = new XMLHttpRequest();
    req.responseType = "json";
    req.withCredentials = false;
    req.onreadystatechange = function () {
      if (req.readyState === 4 && req.status === 200) {
        callback(req.response);
      }
    };

    req.open("GET", url, true);
    req.send(null);
  }
};

function setCookie(name, value, days) {
  const date = new Date();
  date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
  const expires = "expires=" + date.toUTCString();
  document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

function getCookie(name) {
  const cookieName = name + "=";
  const decodedCookie = decodeURIComponent(document.cookie);
  const ca = decodedCookie.split(';');
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) === ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(cookieName) === 0) {
      return c.substring(cookieName.length, c.length);
    }
  }
  return "";
}