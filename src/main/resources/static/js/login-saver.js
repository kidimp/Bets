// Функция для сохранения значений полей формы перед отправкой
function saveFormValues() {
    localStorage.setItem('email', document.getElementById('email').value);
}

// Восстановление значений полей при загрузке страницы
window.onload = function () {
    var savedEmail = localStorage.getItem('email');
    if (savedEmail) {
        document.getElementById('email').value = savedEmail;
    }
};