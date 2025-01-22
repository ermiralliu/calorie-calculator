const pageTable = document.getElementById('food-entry-body');
const paginationButtons = document.getElementById('pagination-buttons');
let buttonsInitialized = false;
let currentPage = -1;

async function fetchData(pageNumber) {
  try {
    const lastPageButton = document.getElementById('paginator'+currentPage);
    lastPageButton?.classList.remove('active');
    currentPage = pageNumber;
    const res = await fetch('/api/food?page=' + pageNumber);
    const resJson = await res.json();
    console.log(resJson);
    const content = resJson.content;
    const totalPages = resJson.totalPages;
    const offset = resJson.pageable.offset;
    pageTable.innerHTML = '';
    content.forEach((row, index) => {
      console.log(row);
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${offset + index + 1}</td>
        <td>${row.name}</td>
        <td>${row.description}</td>
        <td>${row.calories}</td>
        <td>${row.price}</td>
        <td>
          <span>${row.date}</span>
            <br/>
          <span>${row.time}</span>
        </td >
      `;
      pageTable.appendChild(tr);
    });
    if (totalPages < 9) {
      if(buttonsInitialized){
        document.getElementById('paginator'+currentPage).classList.add('active');
        return;
      }
      buttonsInitialized = true;
      pageFor(0, totalPages - 1);
    } else {
      paginationButtons.innerHTML = '';
      let startPage = Math.max(0, currentPage - 3);
      let endPage = Math.min(totalPages - 1, currentPage + 4);
      if (currentPage > 0) {
        createButton(currentPage - 1, 'prev');
      }
      if (currentPage > 3 && totalPages > 8) {
        createButton(0,);
        if (currentPage > 4) {
          const span = document.createElement('span');
          span.classList.add('ellipsis');
          span.textContent = '...';
          paginationButtons.appendChild(span);
        }
      }
      pageFor(startPage, endPage);
      if (currentPage < totalPages - 4 && totalPages > 8) {
        if (currentPage < totalPages - 5) {
          const span = document.createElement('span');
          span.classList.add('ellipsis');
          span.textContent = '...';
          paginationButtons.appendChild(span);
        }
        createButton(totalPages - 1, totalPages);
      }
      // Next button
      if (currentPage < totalPages - 1) {
        createButton(totalPages - 1, 'next');
      }
    }
    document.getElementById('paginator'+currentPage).classList.add('active');
  } catch (e) {
    console.log(e);
  }
}

function pageFor(start, end) {
  for (let i = start; i <= end; i++) {
    createButton(i, i + 1);
  }
}
function createButton(index, title) {
  const but = document.createElement('button');
  but.id = 'paginator'+index;
  but.type = 'button';
  but.textContent = title;
  but.addEventListener('click', () => fetchData(index));
  paginationButtons.appendChild(but);
}

fetchData(0);