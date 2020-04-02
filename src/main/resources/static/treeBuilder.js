function drawCanvas(tree) {
    // console.log(tree);
    let root = tree.root;
    let curNode;
    let temp = 640;
    let arr = [];
    root.lvl = 2;
    arr.push(root);
    root.x = 630;
    root.y = 10;
    let example = document.getElementById("paintField");
    let ctx = example.getContext('2d');
    ctx.clearRect(0,0, 1600, 1000);
    ctx.strokeRect(0, 0, 1280, 800);
    while (arr.length > 0)
    {
        curNode = arr.shift();
        console.log(curNode);
        temp = 280 / curNode.lvl;
        if (curNode.red == true) {
            ctx.fillStyle = 'RED';
            ctx.fillRect(curNode.x, curNode.y, 30, 30);
            ctx.stroke();
            ctx.fillStyle = 'BLACK';
        }
        else
            ctx.strokeRect(curNode.x, curNode.y, 30, 30);
        ctx.fillText(curNode.value, curNode.x + 10, curNode.y + 14);
        ctx.stroke();
        if (curNode.left != null) {
            curNode.left.lvl = curNode.lvl + 1;
            curNode.left.x = curNode.x - temp;
            curNode.left.y = curNode.y + 60;
            arr.push(curNode.left);
            ctx.beginPath();
            ctx.moveTo(curNode.x, curNode.y + 30);
            ctx.lineTo( curNode.left.x + 30, curNode.left.y);
            ctx.closePath();
            ctx.stroke();
        }
        if (curNode.right != null)
        {
            curNode.right.lvl = curNode.lvl + 1;
            curNode.right.x = curNode.x + temp;
            curNode.right.y = curNode.y + 60;
            arr.push(curNode.right);
            ctx.beginPath();
            ctx.moveTo(curNode.x + 30, curNode.y + 30);
            ctx.lineTo(curNode.right.x, curNode.right.y);
            ctx.closePath();
            ctx.stroke();
        }
    }
}

function sendPost_add() {
    let value = document.querySelector("input");
    buttonElement.style.display = 'none';
    if (value.value == ""){
        buttonElement.style.display = 'block';
        button2Element.style.display = 'block';
        return;
    }
    let num = value.value;
    fetch('http://localhost:8080/jsonListener', {
        method: 'post',
        body: JSON.stringify({
            value:num
        })
    }).then(function(response) {
        if(response.ok) {
            response.json().then(function(data) {
                drawCanvas(data);
            });
        } else {
            console.log('Network request for products.json failed with response ' + response.status + ': ' + response.statusText);
        }
    });
    value.value = "";
    buttonElement.style.display = 'block';
}

function sendPost_del() {
    let value = document.querySelector("input");
    buttonElement.style.display = 'none';
    button2Element.style.display = 'none';
    if (value.value == "") {
        buttonElement.style.display = 'block';
        button2Element.style.display = 'block';
        return;
    }
    let num = value.value;
    fetch('http://localhost:8080/jsonListener/del', {
        method: 'post',
        body: JSON.stringify({
            value:num
        })
    }).then(function(response) {
        if(response.ok) {
            response.json().then(function(data) {
                drawCanvas(data);
            });
        } else {
            console.log('Network request for products.json failed with response ' + response.status + ': ' + response.statusText);
        }
    });
    value.value = "";
    buttonElement.style.display = 'block';
    button2Element.style.display = 'block';
}

const buttonElement = document.getElementById('btn');
buttonElement.addEventListener('click', sendPost_add);
const inputPush = document.getElementById('number');
inputPush.addEventListener('keydown', (e) => {
    if (e.key == "Enter")
        sendPost_add();
});
const button2Element = document.getElementById('butn');
button2Element.addEventListener('click', sendPost_del);
