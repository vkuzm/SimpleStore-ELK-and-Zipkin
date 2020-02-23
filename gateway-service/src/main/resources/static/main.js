const cookieName = "cart";
const cookieDuration = 7;

window.onload = function () {
  const pageId = document.body.id;
  switch (pageId) {
    case "index":
      getProducts();
      break;
    case "checkout":
      getProductsInCart();
      break;
  }

  this.makeOrder();
};

function getProducts() {
  const client = new HttpClient();
  client.get('http://localhost:8080/product-service/api/products',
      function (responseData) {
        console.log(responseData);

        responseData.response.forEach(function (product) {
          generateProduct(product);
        });
      });
}

function getProductsInCart() {
  const client = new HttpClient();
  client.post('http://localhost:8080/checkout-service/api/checkout',
      getCartProduct(),
      function (responseData) {
        console.log(responseData);

        responseData.response.forEach(function (product) {
          generateCartProduct(product);
        });
      });
}

function makeOrder() {
  const orderForm = document.getElementById("order-form");

  if (orderForm) {
    orderForm.addEventListener("submit",
        function (e) {
          e.preventDefault();

          const client = new HttpClient();
          const form = document.getElementById("order-form");
          const formData = new FormData(form);
          const requestData = Object.fromEntries(formData);
          requestData.cart = getCartProduct();
          console.log(requestData);

          client.post(
              'http://localhost:8080/checkout-service/api/checkout/makeOrder',
              requestData,
              function (responseData) {
                console.log(responseData);

                if (responseData.status === 200
                    && responseData.response.code === "SUCCESS") {
                  alert(responseData.response.message);
                  //window.location.replace("/");
                  //this.deleteCookie(cookieName);
                } else {
                  alert("Failure has occurred while payment process.");
                }
              });
        });
  }
}

const generateProduct = function (product) {
  const html = '<div class="card mb-4 shadow-sm">'
      + '<div class="card-header"><h4 class="my-0 font-weight-normal">'
      + product.name + '</h4></div>'
      + '<div class="card-body">'
      + '<h1 class="card-title pricing-card-title">$' + product.price + '</h1>'
      + '<p>' + product.description + '</p>'
      + '<button type="button" onclick="addToCart(' + product.productId
      + ')" class="btn btn-lg btn-block btn-primary">Buy</button>'
      + '</div>'
      + '</div>';

  document.getElementById("products").insertAdjacentHTML('beforeend', html);
};

const generateCartProduct = function (product) {
  const html = '<div class="card mb-4 shadow-sm">'
      + '<div class="card-header"><h4 class="my-0 font-weight-normal">'
      + product.name + '</h4></div>'
      + '<div class="card-body">'
      + '<h1 class="card-title pricing-card-title">$' + product.price + '</h1>'
      + '<p>' + product.description + '</p>'
      + '<p>Quantity: ' + product.quantity + '</p>'
      + '</div>';

  document.getElementById("cart-product").insertAdjacentHTML('beforeend', html);
};

const addToCart = function (productId) {
  let isProductExisted = false;
  const productsInCart = this.getCartProduct();

  if (productsInCart.length > 0) {
    updateCart();
  }

  if (!isProductExisted) {
    addToCart();
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

  if (isProductExisted) {
    console.log("Product" + productId + " is updated in the cart!");
    alert("Product" + productId + " is added to the cart!")
  } else {
    console.log("Product" + productId + " is added to the cart!");
    alert("Product" + productId + " is updated in the cart!")
  }
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
  const req = new XMLHttpRequest();
  req.responseType = "json";

  this.get = function (url, callback) {
    req.onreadystatechange = function () {
      if (req.readyState === 4 && req.status === 200) {
        callback(req);
      }
    };

    req.open("GET", url, true);
    req.setRequestHeader("Content-Type", "application/json");
    req.send();
  };

  this.post = function (url, data, callback) {
    req.onreadystatechange = function () {
      if (req.readyState === 4 && req.status === 200) {
        callback(req);
      }
    };

    req.open("POST", url, true);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
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

function deleteCookie(name) {
  document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}