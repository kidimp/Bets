const burgerBtn = document.querySelector('.js_burger-btn');

function toggleBurger() {
  burgerBtn.addEventListener('click', function () {
    DOM.body.classList.toggle(CLASSES.scrollHidden);
    DOM.header.classList.toggle(CLASSES.open);
    this.classList.toggle(CLASSES.active);
  });
}

function closeBurger() {
  if (DOM.header.classList.contains(CLASSES.open)) {
    DOM.body.classList.remove(CLASSES.scrollHidden);
    DOM.header.classList.remove(CLASSES.open);
    burgerBtn.classList.remove(CLASSES.active);
  }

  DOM.header.classList.remove(CLASSES.fixed);
}

function closeBurgerOnMedia() {
  if (!isMediaBreakpoint()) closeBurger();
}

function controlHeaderBurger() {
  toggleBurger();

  window.addEventListener('resize', closeBurgerOnMedia);
}

{ controlHeaderBurger, closeBurger };

const CLASSES = {
  active: 'active',
  loading: 'loading',
  fixed: 'fixed',
  hidden: 'hidden',
  modal: 'modal',
  open: 'open',
  scrollHidden: 'scroll-hidden',
};

const DOM = {
  body: document.querySelector('body'),
  header: document.querySelector('#header')
};

function isMediaBreakpoint(breakpoint = 1199) {
  const mediaBreakpoint = breakpoint;

  if (document.documentElement.clientWidth > mediaBreakpoint) {
    return false;
  }

  return true;
}

controlTable();
controlHeaderBurger();
controlSort();
controlRelativeCheckboxes();
controlModal();

window.addEventListener('resize', controlTable);

function controlModal() {
  const btns = document.querySelectorAll('[data-bs-toggle="modal"]');

  if (!btns.length) return;

  btns.forEach((btn) => {
    btn.addEventListener('click', () => {
      const idToRemove = btn.getAttribute('id');
      const modalId = btn.getAttribute('data-bs-target');

      if (!idToRemove) {
        console.error('id to remove not found');

        return;

      }

      const modal = document.querySelector(modalId);
      const form = modal.querySelector('form');
      const action = form.getAttribute('th:action');
      const newAction = action.replace('_id_', idToRemove);

      form.setAttribute('th:action', newAction);

      modal.addEventListener('hidden.bs.modal', () => {
        const actionReset = action.replace(`id='${idToRemove}'`, 'id=\'_id_\'');
        form.setAttribute('th:action', actionReset);
      });
    });
  });


}

function controlRelativeCheckboxes() {
  const relativeCheckboxes = document.querySelectorAll('.js_relative-checkbox');

  if (!relativeCheckboxes.length) return;

  relativeCheckboxes.forEach((penaltyCheckbox) => {
    penaltyCheckbox.addEventListener('change', () => {
      const requiredSelector = penaltyCheckbox.getAttribute('data-requires');

      if (requiredSelector) {
        const requiredCheckbox = document.querySelector(requiredSelector);

        if (penaltyCheckbox.checked) {
          requiredCheckbox.checked = true;
        }
      }
    });
  });
}

function initializeTable() {
  const tables = document.querySelectorAll('table');

  tables.forEach(table => {
    const tbody = table.querySelector('tbody');
    const rowsArray = Array.from(tbody.querySelectorAll('tr'));

    // Найти колонку с классом 'js_table-sort' и 'rating'
    const ratingColumnIndex = Array.from(table.querySelectorAll('th')).findIndex(th => th.querySelector('.js_table-sort.rating'));

    if (ratingColumnIndex === -1) {
      console.error('Не найдена колонка с классом "rating".');
      return;
    }

    const ratingSortButton = table.querySelector('.js_table-sort.rating');
    const isAverageRatingColumn = ratingSortButton.classList.contains('average-rating');

    // Сортировать строки по значениям в колонке 'rating'
    rowsArray.sort((rowA, rowB) => {
      const cellA = rowA.children[ratingColumnIndex].innerText || rowA.children[ratingColumnIndex].textContent;
      const cellB = rowB.children[ratingColumnIndex].innerText || rowB.children[ratingColumnIndex].textContent;

      const cellAValue = parseFloat(cellA);
      const cellBValue = parseFloat(cellB);

      if (isAverageRatingColumn) {
        // Для средней позиции чем меньше значение, тем выше позиция
        return cellAValue - cellBValue;
      }
        // Для всех остальных чем больше значение, тем выше позиция
        return cellBValue - cellAValue;

    });

    // Пронумеровать строки и сохранить оригинальный порядок
    rowsArray.forEach((row, index) => {
      row.children[0].innerText = index + 1;  // Нумерация строк
      row.dataset.originalOrder = index;      // Сохраняем оригинальный порядок строк
    });

    // Переместить строки в правильном порядке
    rowsArray.forEach(row => tbody.appendChild(row));
  });
}

function controlSort() {
  initializeTable();
  const sortBtns = document.querySelectorAll('.js_table-sort');

  if (!sortBtns.length) return;

  const sortTable = (sortBtn, isAsc) => {
    const table = sortBtn.closest('table');
    const tbody = table.querySelector('tbody');
    const thIndex = Array.from(sortBtn.closest('tr').children).indexOf(sortBtn.parentNode);
    const tableSorts = table.querySelectorAll('.js_table-sort');

    tableSorts.forEach(btn => btn.classList.remove('asc', 'desc'));

    sortBtn.classList.add(isAsc ? 'asc' : 'desc');

    const rowsArray = Array.from(tbody.querySelectorAll('tr'));

    rowsArray.sort((rowA, rowB) => {
      const cellA = rowA.children[thIndex].innerText || rowA.children[thIndex].textContent;
      const cellB = rowB.children[thIndex].innerText || rowB.children[thIndex].textContent;

      const cellAValue = isNaN(cellA) ? cellA : Number(cellA);
      const cellBValue = isNaN(cellB) ? cellB : Number(cellB);

      // Проверка, является ли это колонка "Средняя позиция в рейтингах"
      const isAverageRatingColumn = sortBtn.classList.contains('average-rating');

      if (isAverageRatingColumn) {
        // Для "Средняя позиция в рейтингах" чем меньше значение, тем выше позиция
        if (cellAValue < cellBValue) {
          return isAsc ? 1 : -1;
        }
        if (cellAValue > cellBValue) {
          return isAsc ? -1 : 1;
        }
      } else {
        // Для всех остальных колонок чем выше значение, тем выше позиция
        if (cellAValue < cellBValue) {
          return isAsc ? -1 : 1;
        }
        if (cellAValue > cellBValue) {
          return isAsc ? 1 : -1;
        }
      }
      return 0;
    });

    // Очистка и добавление отсортированных строк в tbody
    tbody.innerHTML = '';
    rowsArray.forEach(row => tbody.appendChild(row));
  };

  sortBtns.forEach((sortBtn) => {
    sortBtn.addEventListener('click', () => {
      const isAsc = !sortBtn.classList.contains('asc');
      sortTable(sortBtn, isAsc);
    });

    // Сортировка по умолчанию
    if (sortBtn.hasAttribute('data-default-sort')) {
      const defaultSortDirection = sortBtn.getAttribute('data-default-sort') === 'asc';
      sortTable(sortBtn, defaultSortDirection);
    }
  });
}



function controlTable() {
  const table = document.querySelector('table');

  if (!table) return;

  const thead = table.querySelector('thead');
  const tbody = table.querySelector('tbody');
  const tableContainer = table.parentElement;

  const fixedTheadColumns = thead.querySelectorAll('.fixed-column');
  let leftTh = 0;

  if (!fixedTheadColumns.length) return;

  fixedTheadColumns.forEach((column, index) => {
    column.style.left = `${leftTh}px`;
    leftTh += column.offsetWidth;

    if (index === fixedTheadColumns.length - 1) {
      column.classList.add('last-fixed-column');
    }
  });

  const rows = tbody.querySelectorAll('tr');
  rows.forEach((row) => {
    const fixedColumns = row.querySelectorAll('.fixed-column');
    let left = 0;

    fixedColumns.forEach((column, index) => {
      column.style.left = `${left}px`;
      left += column.offsetWidth;

      if (index === fixedColumns.length - 1) {
        column.classList.add('last-fixed-column');
      }
    });
  });

  function updateShadow() {
    const { scrollLeft } = tableContainer;

    if (scrollLeft > 0) {
      document.querySelectorAll('.last-fixed-column').forEach((column) => {
        column.classList.add('scroll-shadow');
      });
    } else {
      document.querySelectorAll('.last-fixed-column').forEach((column) => {
        column.classList.remove('scroll-shadow');
      });
    }
  }

  tableContainer.addEventListener('scroll', updateShadow);

  updateShadow();
}

function activateTab(hash) {
  // Убираем класс active у всех табов и их контента
  document.querySelectorAll('.tabs__item').forEach((tab) => {
    tab.classList.remove('active');
  });
  document.querySelectorAll('.js_tabs-item').forEach((content) => {
    content.classList.remove('active');
  });

  // Добавляем класс active к текущему табу и его контенту
  if (hash) {
    const activeTabLink = document.querySelector(`.tabs__link[href="${hash}"]`);
    if (activeTabLink) {
      activeTabLink.parentElement.classList.add('active');
      document.querySelector(hash).classList.add('active');
    }
  }
}

function controlTabs() {
  // Слушаем клики по табам
  document.querySelectorAll('.tabs__link').forEach((tabLink) => {
    tabLink.addEventListener('click', function (event) {
      event.preventDefault();
      const hash = this.getAttribute('href');
      window.history.pushState(null, null, hash);
      activateTab(hash);
    });
  });

  // Слушаем изменения в URL
  window.addEventListener('hashchange', () => {
    activateTab(window.location.hash);
  });

  // Активируем таб при загрузке страницы
  if (window.location.hash) {
    activateTab(window.location.hash);
  } else {
    // Если нет хэштега в URL, активируем первый таб
    activateTab('#tab-table');
  }
}
