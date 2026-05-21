// Общее меню отчётов для всех страниц
document.addEventListener('DOMContentLoaded', function() {
    // Находим ссылку "Отчёты"
    var reportsToggle = document.querySelector('.reports-toggle');
    var reportsSubmenu = document.getElementById('reportsSubmenu');
    var reportsArrow = document.getElementById('reportsArrow');

    if (reportsToggle && reportsSubmenu) {
        // Клик по "Отчёты"
        reportsToggle.addEventListener('click', function(e) {
            e.preventDefault();

            if (reportsSubmenu.style.display === 'block') {
                reportsSubmenu.style.display = 'none';
                if (reportsArrow) {
                    reportsArrow.className = 'fas fa-chevron-down';
                }
            } else {
                reportsSubmenu.style.display = 'block';
                if (reportsArrow) {
                    reportsArrow.className = 'fas fa-chevron-up';
                }
            }
        });

        // Закрытие при клике вне меню
        document.addEventListener('click', function(e) {
            if (!reportsToggle.contains(e.target) && !reportsSubmenu.contains(e.target)) {
                reportsSubmenu.style.display = 'none';
                if (reportsArrow) {
                    reportsArrow.className = 'fas fa-chevron-down';
                }
            }
        });
    }
});