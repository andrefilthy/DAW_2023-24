import * as React from 'react'
import { useEffect } from 'react'
import '../game.css'
import { Cell, CellPosition } from './Cell'

export type BoardType = {
    cells: string,
    username: string
}

export type BoardProps = {
    board : BoardType,
    selectedPiece : Cell
}

export function Board({board, selectedPiece} : BoardProps) : React.ReactElement {

    const cellElements =  <div></div> 
    const BOARD_SIZE = 10

    useEffect(() => { createCells()})
 
    return(
        <div className="board">
            <div id="board-grid">
                {cellElements}
            </div>
        </div>
    )

    function createCells(){
        const cells = []
        for(let i = 0; i < BOARD_SIZE; i++){
            for(let j = 0; j < BOARD_SIZE; j++){
                let className = getCellClass(board.cells.charAt(BOARD_SIZE * i + j))
                className = className.concat(" clickable-cell")
                cells.push(<Cell position={{l: i, c: j}} className={className} onClick={onClickCell}></Cell>)
            }
        }
    }

    function onClickCell(position : CellPosition){
        if(!!selectedPiece){
            if(isPositionAvailable(position)){
                // TODO ( Implentar a logica de colocar a peca )
                //placePiece(selectedPiece)
            }
            return
        }
    }

    function isPositionAvailable(position : CellPosition) : boolean{
        // TODO (Implementar a logica de verificacao de se esta a colocar uma pece no lugar de outra)
        if(position.c <= BOARD_SIZE && position.l <= BOARD_SIZE) return true
        else return false
    }
}

function getCellClass(c : string){
    switch(c){
        case('-'): return 'empty-cell'
        case('W'): return 'whitepiece-cell'
        case('B'): return 'blackpiece-cell'
    }
}

