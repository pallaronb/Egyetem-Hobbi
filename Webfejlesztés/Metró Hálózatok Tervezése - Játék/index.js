const rules = document.querySelector("#rules");
const backtomenu = document.querySelector("#backtomenu")
const start = document.querySelector("#start")
const menu = document.querySelector("#menu-container");
const rulebook = document.querySelector("#rulebook");
const grid = document.querySelector("#grid");
const gameBoard = document.querySelector("#gameboard")
const playerNameBox = document.querySelector("#player-name");
const tomenu = document.querySelector("#tomenu");
const getPlayerName = document.querySelector("#get-player-name")
const lines = [
    { "id": 0, "name": "M1", "color": "#FFD800", "start": 19 },
    { "id": 1, "name": "M2", "color": "#E41F18", "start": 28 },
    { "id": 2, "name": "M3", "color": "#005CA5", "start": 3 },
    { "id": 3, "name": "M4", "color": "#4CA22F", "start": 39 }
];

const stations = [
    { "id": 0, "x": 0, "y": 0, "type": "A", "train": false, "side": "Buda", "district": 0 },
    { "id": 1, "x": 2, "y": 0, "type": "B", "train": false, "side": "Buda", "district": 1 },
    { "id": 2, "x": 4, "y": 0, "type": "D", "train": false, "side": "Buda", "district": 2 },
    { "id": 3, "x": 6, "y": 0, "type": "D", "train": false, "side": "Pest", "district": 2 },
    { "id": 4, "x": 7, "y": 0, "type": "B", "train": false, "side": "Pest", "district": 3 },
    { "id": 5, "x": 9, "y": 0, "type": "C", "train": false, "side": "Pest", "district": 4 },
    { "id": 6, "x": 0, "y": 1, "type": "D", "train": false, "side": "Buda", "district": 1 },
    { "id": 7, "x": 1, "y": 1, "type": "B", "train": false, "side": "Buda", "district": 1 },
    { "id": 8, "x": 5, "y": 1, "type": "A", "train": false, "side": "Buda", "district": 2 },
    { "id": 9, "x": 8, "y": 1, "type": "C", "train": false, "side": "Pest", "district": 3 },
    { "id": 10, "x": 9, "y": 1, "type": "A", "train": false, "side": "Pest", "district": 3 },
    { "id": 11, "x": 2, "y": 2, "type": "D", "train": false, "side": "Buda", "district": 1 },
    { "id": 12, "x": 4, "y": 2, "type": "D", "train": false, "side": "Buda", "district": 2 },
    { "id": 13, "x": 5, "y": 2, "type": "D", "train": false, "side": "Buda", "district": 3 },
    { "id": 14, "x": 6, "y": 2, "type": "C", "train": false, "side": "Pest", "district": 3 },
    { "id": 15, "x": 9, "y": 2, "type": "D", "train": true, "side": "Pest", "district": 4 },
    { "id": 16, "x": 0, "y": 3, "type": "C", "train": false, "side": "Buda", "district": 5 },
    { "id": 17, "x": 2, "y": 3, "type": "B", "train": false, "side": "Buda", "district": 5 },
    { "id": 18, "x": 3, "y": 3, "type": "C", "train": false, "side": "Buda", "district": 6 },
    { "id": 19, "x": 7, "y": 3, "type": "A", "train": false, "side": "Pest", "district": 7 },
    { "id": 20, "x": 8, "y": 3, "type": "D", "train": false, "side": "Pest", "district": 7 },
    { "id": 21, "x": 0, "y": 4, "type": "B", "train": false, "side": "Buda", "district": 5 },
    { "id": 22, "x": 3, "y": 4, "type": "A", "train": false, "side": "Buda", "district": 6 },
    { "id": 23, "x": 4, "y": 4, "type": "B", "train": false, "side": "Buda", "district": 6 },
    { "id": 24, "x": 5, "y": 4, "type": "C", "train": false, "side": "Pest", "district": 6 },
    { "id": 25, "x": 6, "y": 4, "type": "A", "train": true, "side": "Pest", "district": 6 },
    { "id": 26, "x": 9, "y": 4, "type": "A", "train": false, "side": "Pest", "district": 7 },
    { "id": 27, "x": 0, "y": 5, "type": "A", "train": false, "side": "Buda", "district": 5 },
    { "id": 28, "x": 2, "y": 5, "type": "C", "train": false, "side": "Buda", "district": 5 },
    { "id": 29, "x": 5, "y": 5, "type": "D", "train": false, "side": "Pest", "district": 6 },
    { "id": 30, "x": 6, "y": 5, "type": "?", "train": false, "side": "Pest", "district": 6 },
    { "id": 31, "x": 9, "y": 5, "type": "B", "train": false, "side": "Pest", "district": 7 },
    { "id": 32, "x": 1, "y": 6, "type": "C", "train": false, "side": "Buda", "district": 5 },
    { "id": 33, "x": 3, "y": 6, "type": "D", "train": true, "side": "Buda", "district": 6 },
    { "id": 34, "x": 6, "y": 6, "type": "B", "train": false, "side": "Pest", "district": 6 },
    { "id": 35, "x": 7, "y": 6, "type": "D", "train": false, "side": "Pest", "district": 7 },
    { "id": 36, "x": 8, "y": 6, "type": "C", "train": true, "side": "Pest", "district": 7 },
    { "id": 37, "x": 0, "y": 7, "type": "B", "train": false, "side": "Buda", "district": 9 },
    { "id": 38, "x": 3, "y": 7, "type": "A", "train": false, "side": "Buda", "district": 10 },
    { "id": 39, "x": 4, "y": 7, "type": "B", "train": false, "side": "Buda", "district": 10 },
    { "id": 40, "x": 6, "y": 7, "type": "B", "train": false, "side": "Pest", "district": 10 },
    { "id": 41, "x": 9, "y": 7, "type": "A", "train": false, "side": "Pest", "district": 11 },
    { "id": 42, "x": 1, "y": 8, "type": "A", "train": false, "side": "Buda", "district": 9 },
    { "id": 43, "x": 2, "y": 8, "type": "B", "train": false, "side": "Buda", "district": 9 },
    { "id": 44, "x": 5, "y": 8, "type": "C", "train": false, "side": "Buda", "district": 10 },
    { "id": 45, "x": 8, "y": 8, "type": "D", "train": false, "side": "Pest", "district": 11 },
    { "id": 46, "x": 0, "y": 9, "type": "D", "train": false, "side": "Buda", "district": 8 },
    { "id": 47, "x": 2, "y": 9, "type": "C", "train": false, "side": "Buda", "district": 9 },
    { "id": 48, "x": 3, "y": 9, "type": "A", "train": true, "side": "Buda", "district": 10 },
    { "id": 49, "x": 6, "y": 9, "type": "D", "train": false, "side": "Buda", "district": 10 },
    { "id": 50, "x": 7, "y": 9, "type": "A", "train": false, "side": "Pest", "district": 11 },
    { "id": 51, "x": 8, "y": 9, "type": "C", "train": false, "side": "Pest", "district": 11 },
    { "id": 52, "x": 9, "y": 9, "type": "B", "train": false, "side": "Pest", "district": 12 }
];

function toMenu() {
    const name = playerNameBox.value.trim();
    if (name.length > 0) {
        const existingError = getPlayerName.querySelector(".error-message");
        if (existingError) {
            existingError.remove();
        }
        getPlayerName.classList.add("hidden");
        menu.classList.remove("hidden");
    } else {
        const errorMsg = document.createElement("p");
        errorMsg.textContent = "Kérlek add meg a neved a játék megkezdéséhez!";
        errorMsg.style.color = "red";
        errorMsg.classList.add("error-message");
        if (!getPlayerName.querySelector(".error-message")) {
            getPlayerName.prepend(errorMsg);
        }
    }
    gameState.playerName = name;
}

function toggleMenu(showMenu) {
    if (showMenu) {
        menu.classList.remove("hidden");
        rulebook.classList.add("hidden");
    }
    else {
        menu.classList.add("hidden");
        rulebook.classList.remove("hidden");
    }
}

function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
}

const FULL_DECK_SIMPLE = ['A', 'B', 'C', 'D', 'Joker', 'A', 'B', 'C', 'D', 'Joker'];

let gameState = {
    playerName: '',
    timeElapsed: 0,
    timerInterval: null,
    lineOrder: [],
    currentLineIndex: 0,
    linesCompleted: 0,
    cardsDrawnThisRound: 0,
    currentCard: null,
    currentDeck: [],
    metroLines: {},
    trainScorePosition: 0,
    trainScoreTable: [0, 1, 2, 4, 6, 8, 11, 14, 17, 21, 25],
    totalScore: 0,
    selectedEndpoint: null,
};

function startGame() {
    initializeGame();

    menu.classList.add("hidden");
    gameBoard.classList.remove("hidden")

    let infoDiv = document.querySelector("#game-info");
    if (!infoDiv) {
        infoDiv = document.createElement('div');
        infoDiv.id = 'game-info';
        document.querySelector("#game-map-container").prepend(infoDiv);
    }

    drawGameboard(false);
    updateGameInfo();
}

function initializeGame() {
    if (gameState.timerInterval) {
        clearInterval(gameState.timerInterval);
    }
    gameState.timeElapsed = 0;
    gameState.currentLineIndex = 0;
    gameState.cardsDrawnThisRound = 0;
    gameState.currentCard = null;
    gameState.trainScorePosition = 0;
    gameState.totalScore = 0;
    gameState.linesCompleted = 0;

    gameState.lineOrder = lines.map(line => line.name);
    shuffleArray(gameState.lineOrder);

    gameState.metroLines = {};
    lines.forEach(line => {
        gameState.metroLines[line.name] = {
            color: line.color,
            segments: [],
            endpoints: [line.start],
            stationsVisited: new Set([line.start]),
            roundScore: 0
        };
    });

    gameState.currentDeck = [...FULL_DECK_SIMPLE];
    shuffleArray(gameState.currentDeck);
    gameState.timerInterval = setInterval(updateTimer, 1000);

    return true;
}

function drawAllLines() {
    const layer = document.querySelector("#line-layer");
    layer.innerHTML = '';

    const cellWidth = grid.clientWidth / 10;
    const cellHeight = grid.clientHeight / 10;

    for (const lineName in gameState.metroLines) {
        const lineData = gameState.metroLines[lineName];

        lineData.segments.forEach(segment => {
            const startStation = stations.find(s => s.id === segment.start);
            const endStation = stations.find(s => s.id === segment.end);
            if (!startStation || !endStation) return;

            const x1 = startStation.x * cellWidth + cellWidth / 2;
            const y1 = startStation.y * cellHeight + cellHeight / 2;
            const x2 = endStation.x * cellWidth + cellWidth / 2;
            const y2 = endStation.y * cellHeight + cellHeight / 2;

            const line = document.createElement('div');
            line.style.position = 'absolute';
            line.style.backgroundColor = lineData.color;
            line.style.transformOrigin = '0 0';
            
            const dx = x2 - x1;
            const dy = y2 - y1;
            const length = Math.sqrt(dx*dx + dy*dy);
            const angle = Math.atan2(dy, dx) * 180 / Math.PI;
            
            line.style.width = length + 'px';
            line.style.height = '4px';
            line.style.top = y1 + 'px';
            line.style.left = x1 + 'px';
            line.style.transform = `rotate(${angle}deg)`;
            
            layer.appendChild(line);
        });
    }
}


function drawGameboard(isOver) {
    const colors = [...Array(10)].map(() => Array(10).fill(null));
    if (isOver) {
        const oldCells = Array.from(grid.children);
        for (let y = 0; y < 10; y++) {
            for (let x = 0; x < 10; x++) {
                const index = y * 10 + x;
                const oldCell = oldCells[index];
                if (oldCell) {
                    colors[x][y] = oldCell.style.backgroundColor || null;
                }
            }
        }
    }

    grid.innerHTML = "";
    grid.style.gridTemplateColumns = "repeat(10, 1fr)";
    grid.style.gridTemplateRows = "repeat(10, 1fr)";
    grid.style.maxWidth = "500px";
    grid.style.aspectRatio = "1 / 1";

    const startStationIds = lines.map(line => line.start);
    const currentLineName = gameState.lineOrder[gameState.currentLineIndex];
    const currentLine = gameState.metroLines[currentLineName];

    for (let y = 0; y < 10; y++) {
        for (let x = 0; x < 10; x++) {

            const cell = document.createElement("div");
            cell.classList.add("cell");
            cell.dataset.x = x;
            cell.dataset.y = y;

            if (colors[x][y]) {
                cell.style.backgroundColor = colors[x][y]
            }

            const station = stations.find(s => s.x === x && s.y === y);
            if (station) {
                cell.classList.add("station");
                cell.dataset.stationId = station.id;

                if (startStationIds.includes(station.id)) {
                    const starter = lines.find(l => l.start === station.id);
                    cell.style.backgroundColor = starter.color;
                }

                if (currentLine.stationsVisited.has(station.id)) {
                    if (!cell.dataset.visited) {
                        cell.style.backgroundColor = currentLine.color;
                        cell.dataset.visited = true;
                    }
                }

                cell.textContent = station.type === '?' ? 'J' : station.type;

                cell.addEventListener('click', handleCellClick);

                if (station.train) {
                    cell.classList.add('train-station');
                }

            } else {
                cell.classList.add("filler");
                cell.textContent = "";
            }
            grid.appendChild(cell);
        }
    }
    drawAllLines();
    highlightPossibleStations();
}
function updateGameInfo() {
    let infoDiv = document.querySelector("#game-info");
    if (!infoDiv) {
        infoDiv = document.createElement("div");
        infoDiv.id = "game-info";
        document.querySelector("#game-map-container").prepend(infoDiv);
    }

    const currentLineName = gameState.lineOrder[gameState.currentLineIndex];
    const currentLineData = lines.find(line => line.name === currentLineName);
    const currentLineColor = currentLineData ? currentLineData.color : 'black';

    const minutes = String(Math.floor(gameState.timeElapsed / 60)).padStart(2, '0');
    const seconds = String(gameState.timeElapsed % 60).padStart(2, '0');
    const timeString = `${minutes}:${seconds}`;

    const timerDisplay = document.querySelector("#timer-display");
    const cardDisplay = document.querySelector("#current-card-display");
    if (timerDisplay) timerDisplay.textContent = timeString;
    if (cardDisplay) cardDisplay.textContent = gameState.currentCard || '---';

    infoDiv.innerHTML = `
        <p>Játékos: ${gameState.playerName}</p>
        <p>Aktuális vonal: ${currentLineName} 
           (Szín: <span style="color: ${currentLineColor}; font-size: 1.5em;">■</span>)</p>
        <p>Kártyák ebben a fordulóban: ${gameState.cardsDrawnThisRound} / 8</p>
        <div id="action-buttons">
            <button id="draw-card">Kártya húzása</button>
            <button id="next-round" class="hidden">Következő forduló / Játék vége</button>
        </div>
        <p id="error-message" style="color: red;"></p>
    `;

    document.querySelector('#draw-card').addEventListener('click', drawCard);
    document.querySelector('#next-round').addEventListener('click', startNextRound);

    if (gameState.cardsDrawnThisRound >= 8 || gameState.currentLineIndex >= lines.length) {
        document.querySelector('#draw-card')?.classList.add("hidden");
        document.querySelector('#next-round')?.classList.remove("hidden");
        if (gameState.linesCompleted >= lines.length) {
            document.querySelector('#next-round').textContent = "Új játék!";
        }
    } else {
        document.querySelector('#draw-card')?.classList.remove("hidden");
        document.querySelector('#next-round')?.classList.add("hidden");
        document.querySelector('#next-round').textContent = "Következő forduló";
    }
}
function updateTimer() {
    gameState.timeElapsed++;
    updateGameInfo();
}
function highlightPossibleStations() {
    document.querySelectorAll('.highlight, .possible-target').forEach(el => el.classList.remove('highlight', 'possible-target'));

    const currentLineName = gameState.lineOrder[gameState.currentLineIndex];
    const currentLine = gameState.metroLines[currentLineName];

    if (!currentLine || gameState.cardsDrawnThisRound === 0) return;

    currentLine.endpoints.forEach(endpointId => {
        const endpointCell = document.querySelector(`[data-station-id="${endpointId}"]`);
        if (endpointCell) {
            endpointCell.classList.add('highlight');
        }
    });

    if (gameState.selectedEndpoint !== null) {
        const neighbors = findNeighbors(gameState.selectedEndpoint);
        neighbors.forEach(neighbor => {
            const targetCell = document.querySelector(`[data-station-id="${neighbor.id}"]`);
            if (targetCell) {
                targetCell.classList.add('possible-target');
            }
        });
    }
}

function handleCellClick(e) {
    const targetCell = e.target.closest('.station');
    const errorDisplay = document.querySelector('#game-info p#error-message');

    if (!targetCell || gameState.cardsDrawnThisRound === 0 || gameState.currentCard === null) {
        if (errorDisplay && !targetCell) errorDisplay.textContent = 'Húzz kártyát, mielőtt építesz!';
        return;
    }

    const stationId = parseInt(targetCell.dataset.stationId);
    const currentLineName = gameState.lineOrder[gameState.currentLineIndex];
    const currentLine = gameState.metroLines[currentLineName];

    if (gameState.selectedEndpoint === null) {
        if (currentLine.endpoints.includes(stationId)) {
            gameState.selectedEndpoint = stationId;
            highlightPossibleStations();
        } else {
            if (errorDisplay) errorDisplay.textContent = 'Csak a metróvonal végpontjára építhetsz!';
        }
        return;
    }

    else if (gameState.selectedEndpoint !== null) {
        const isTargetValid = targetCell.classList.contains('possible-target');

        if (isTargetValid) {
            if (currentLine.stationsVisited.has(stationId)) {
                if (errorDisplay) errorDisplay.textContent = 'Ez az állomás már érintett a vonalon!';
                return;
            }

            const newSegment = {
                start: gameState.selectedEndpoint,
                end: stationId,
                line: currentLineName
            };
            currentLine.segments.push(newSegment);
            drawAllLines();
            currentLine.stationsVisited.add(stationId);

            const oldEndpointIndex = currentLine.endpoints.indexOf(gameState.selectedEndpoint);
            if (oldEndpointIndex > -1) {
                currentLine.endpoints.splice(oldEndpointIndex, 1);
            }
            currentLine.endpoints.push(stationId);

            gameState.currentCard = null;
            gameState.selectedEndpoint = null;
            gameState.selectedTargetStation = null;

            const targetStationData = stations.find(s => s.id === stationId);
            if (targetStationData && targetStationData.train && gameState.trainScorePosition < gameState.trainScoreTable.length - 1) {
                gameState.trainScorePosition++;
            }

            drawGameboard(true);
            updateGameInfo();
            if (errorDisplay) errorDisplay.textContent = 'Szakasz építve!';

        } else {
            if (errorDisplay) errorDisplay.textContent = 'Érvénytelen célpont! Válassz egy szomszédos, kártyának megfelelő állomást, vagy kattints a másik végpontra a váltáshoz.';
            gameState.selectedEndpoint = null;
            highlightPossibleStations();
        }
    }
}
function drawCard() {
    if (gameState.cardsDrawnThisRound >= 8) {
        return;
    }

    if (gameState.currentDeck.length === 0) {
        gameState.currentDeck = [...FULL_DECK_SIMPLE];
        shuffleArray(gameState.currentDeck);
    }

    gameState.currentCard = gameState.currentDeck.pop();
    gameState.cardsDrawnThisRound++;

    gameState.selectedEndpoint = null;
    highlightPossibleStations();
    updateGameInfo();
}

function calculateRoundScore() {
    return 0;
}

function startNextRound() {
    if (gameState.currentLineIndex >= lines.length - 1) {
        clearInterval(gameState.timerInterval);
        const finalScore = gameState.totalScore;
        const errorDisplay = document.querySelector('#game-info p#error-message');
        if (errorDisplay) {
            errorDisplay.textContent = `Játék Vége! Összpontszám: ${finalScore}`;
        }
        return;
    }

    gameState.metroLines[gameState.lineOrder[gameState.currentLineIndex]].roundScore = calculateRoundScore();

    gameState.linesCompleted++;
    gameState.currentLineIndex++;
    gameState.cardsDrawnThisRound = 0;
    gameState.currentCard = null;
    gameState.selectedEndpoint = null;

    gameState.currentDeck = [...FULL_DECK_SIMPLE];
    shuffleArray(gameState.currentDeck);
    drawGameboard(true);
    updateGameInfo();
}

function findNeighbors(stationId) {
    const currentStation = stations.find(s => s.id === stationId);
    if (!currentStation) return [];

    const x = currentStation.x;
    const y = currentStation.y;

    const neighbors = [];
    const possibleCoords = [
        { x: x + 1, y: y, direction: 'E' },
        { x: x - 1, y: y, direction: 'W' },
        { x: x, y: y + 1, direction: 'S' },
        { x: x, y: y - 1, direction: 'N' },
        { x: x + 1, y: y + 1, direction: 'NE' },
        { x: x - 1, y: y + 1, direction: 'NW' },
        { x: x + 1, y: y - 1, direction: 'SE' },
        { x: x - 1, y: y - 1, direction: 'SW' },
        { x: x + 2, y: y, direction: 'EE' },
        { x: x - 2, y: y, direction: 'WW' },
        { x: x, y: y - 2, direction: 'SS' },
        { x: x, y: y + 2, direction: 'NN' },
        { x: x + 2, y: y + 2, direction: 'NNE' },
        { x: x + 2, y: y - 2, direction: 'NNW' },
        { x: x - 2, y: y - 2, direction: 'SSW' },
        { x: x - 2, y: y + 2, direction: 'SSE' },
        { x: x + 3, y: y, direction: 'EEE' },
        { x: x - 3, y: y, direction: 'WWW' },
        { x: x, y: y - 3, direction: 'SSS' },
        { x: x, y: y + 3, direction: 'NNN' },
        { x: x + 3, y: y + 3, direction: 'NNNE' },
        { x: x + 3, y: y - 3, direction: 'NNNW' },
        { x: x - 3, y: y - 3, direction: 'SSSW' },
        { x: x - 3, y: y + 3, direction: 'SSSE' },

    ];
    function stationAt(xx, yy) {
        return stations.find(s => s.x === xx && s.y === yy);
    }

    function isBlocked(fromX, fromY, toX, toY) {
        const dx = toX - fromX;
        const dy = toY - fromY;

        const adx = Math.abs(dx);
        const ady = Math.abs(dy);

        if (adx <= 1 && ady <= 1) return false;

        const steps = Math.max(adx, ady);
        const stepX = dx / steps;
        const stepY = dy / steps;

        for (let i = 1; i < steps; i++) {
            const mx = fromX + stepX * i;
            const my = fromY + stepY * i;

            if (stationAt(mx, my)) return true;
        }

        return false;
    }


    for (const coord of possibleCoords) {
        const neighbor = stationAt(coord.x, coord.y);
        if (!neighbor) {
            continue;
        }
        if (isBlocked(x, y, coord.x, coord.y)) {
            continue;
        }

        const neighborType = neighbor.type === '?' ? 'Joker' : neighbor.type;

        if (!gameState.currentCard || gameState.currentCard === neighborType || gameState.currentCard === 'Joker') {
            neighbors.push(neighbor);
        }
    }
    return neighbors;
}

tomenu.addEventListener("click", toMenu);
start.addEventListener("click", startGame);
rules.addEventListener("click", () => toggleMenu(false));
backtomenu.addEventListener("click", () => toggleMenu(true));