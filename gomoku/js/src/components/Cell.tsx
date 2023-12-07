import * as React from 'react'

type CellProps = {
    position :  CellPosition
    className : string
    onClick? : (position : CellPosition) => void | null
    onHover? : (position : CellPosition) => void | null
}

export type CellPosition = {
    l : number,
    c : number
}

export function Cell({position, onClick = null, className, onHover = null} : CellProps) : React.ReactElement {
    className = className.concat(" board-cell")
    if(!!onClick){
        className.concat(" clickable-cell")
    }

    if(!!onClick && !!onHover) {
        return(
            <div className={className} onClick={() => onClick(position)} onMouseOver={() => onHover(position)}></div>
        )
    }
    else if(!!onClick) {
        return(
            <div className={className} onClick={() => onClick(position) }></div>
        )
    }
    else if(!!onHover){
        return(
            <div className={className} onMouseOver={() => onHover(position) }></div>
        )
    }
    
    return(
        <div className={className}></div>
    )
}