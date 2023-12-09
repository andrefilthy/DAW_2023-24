import * as React from 'react'

export type Cell = {
    position :  CellPosition
    className : string
    onClick? : (position : CellPosition) => void | null
}

export type CellPosition = {
    l : number,
    c : number
}

export function Cell({position, className, onClick} : Cell) : React.ReactElement {
    className = className.concat(" board-cell")
    if(!!onClick) {
        className.concat(" clickable-cell")
        return( <div className={className} onClick={() => onClick(position) }></div> )
    }
    return(
        <div className={className}></div>
    )
}