function selectCheckbox(checkbox) {
    let checkboxes = document.querySelectorAll('input[name="x"]');
    checkboxes.forEach(cb => cb.checked = false);
    checkbox.checked = true;
}

function setR(button) {
    // Удаляем подсветку со всех кнопок R
    let buttons = document.querySelectorAll('input[name="r1"], input[name="r2"], input[name="r3"], input[name="r4"], input[name="r5"]');
    buttons.forEach(btn => btn.classList.remove('selected'));

    // Добавляем подсветку на нажатую кнопку
    button.classList.add('selected');

    // Сохраняем значение R в скрытое поле
    document.getElementById('selectedR').value = button.value;
}

function checkForm(element) {
    let x = document.querySelector('input[name="x"]:checked').value;
    let y = document.getElementById('y').value;
    let r = document.getElementById('selectedR').value;

    if (!x || isNaN(y) || isNaN(r) || y < -5 || y > 3) {
        alert("Пожалуйста, введите корректные значения.");
        return false;
    }
    
    let dataToServer = {
        x: x,
        y: y,
        r: r
    }

    fetch('http://localhost:8080/fcgi-bin/fcgi_server.jar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToServer),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log("Ответ от сервера:", data);
        let result = data.result;
        let row = document.createElement('tr');
        row.innerHTML = `
        <td>${result=="true" ? '<span style="color: #05da00">&#9745;</span>' : result=="false" ? '<span style="color: red">&#9746;</span>' : result}</td>
        <td>${x}</td>
        <td>${y}</td>
        <td>${r}</td>
        <td>${data.workTime} мс</td>
        <td>${data.time}</td>
        
        `;
        let tableBody = document.getElementById('stats-table').querySelector('tbody');
        tableBody.prepend(row);

        if (tableBody) {
            localStorage.setItem('tableData', tableBody.innerHTML);
        }
    })
    .catch(error => {
        console.error('Ошибка:', error);
    });

    return false;
}



window.onload = function() {
    let tableBody = document.getElementById('stats-table').querySelector('tbody');
    if (localStorage.getItem('tableData')) {
        let tableData = localStorage.getItem('tableData');
        if (tableData) {
            tableBody.innerHTML = tableData;
        }
    }
};