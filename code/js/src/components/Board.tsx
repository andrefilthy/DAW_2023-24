import * as React from 'react'
import { useEffect, useState } from 'react'
import '../game.css'
import { Cell, CellPosition } from './Cell'

export type BoardType = {
    cells: string,
    username: string
}

export type BoardProps = {
    board : BoardType,
    selectedPiece? : Cell,
    placePiece? : (piece : Cell) => void
    play? : (position: CellPosition) => void
}

export function Board({board, selectedPiece, placePiece = null, play = null} : BoardProps) : React.ReactElement {

    const [cellElements, setCells] = useState<React.ReactElement[]>(null)

    const BOARD_SIZE = 15

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
                if(!!selectedPiece){
                    className = className.concat(" clickable-cell")
                    cells.push(<Cell position={{l: i, c: j}} className={className} onHover={onHoverCell} onClick={onClickCell}></Cell>)
                }
                else if(!!play){
                    className = className.concat(" clickable-cell")
                    cells.push(<Cell position={{l: i, c: j}} className={className} onClick={play}></Cell>)
                }else{
                    cells.push(<Cell position={{l: i, c: j}} className={className}></Cell>)
                }
            }
        }
        setCells(cells)
    }


    function onHoverCell(position : CellPosition){
        const cells = []
        for(let i = 0; i < BOARD_SIZE; i++){
            for(let j = 0; j < BOARD_SIZE; j++){
                let className = getCellClass(board.cells.charAt(BOARD_SIZE * i + j))
                className = className.concat(" clickable-cell")
                let isAvailable : boolean = isPositionAvailable(position)
                if(!isAvailable){
                    cells.push(<Cell position={{l: i, c: j}} className={className} onHover={onHoverCell} onClick={onClickCell}></Cell>)
                }  
            }
        }
        setCells(cells)
    }

    function onClickCell(position : CellPosition){
        if(!!selectedPiece){
            if(isPositionAvailable(position)){
                placePiece(selectedPiece)
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

