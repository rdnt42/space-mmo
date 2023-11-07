export function getRelativeX(currX, absX) {
    return currX - absX + window.innerWidth / 2;
}

export function getRelativeY(currY, absY) {
    return currY - absY + window.innerHeight / 2;
}