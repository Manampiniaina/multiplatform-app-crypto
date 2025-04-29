  document.addEventListener("DOMContentLoaded", function () {
    const sellLinks = document.querySelectorAll(".sell-link");
    const cryptoNameSpan = document.getElementById("cryptoName");

    sellLinks.forEach(link => {
      link.addEventListener("click", function () {
        const cryptoName = this.getAttribute("data-crypto");
        cryptoNameSpan.textContent = cryptoName;
      });
    });
  });
