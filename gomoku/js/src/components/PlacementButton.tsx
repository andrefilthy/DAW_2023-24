import * as React from 'react'

type PlacementProps = {
    rotate : () => void,
    cancel : () => void,
}

export function PlacementButton({rotate, cancel} : PlacementProps) : React.ReactElement{
    return(
        <div>
            <button onClick={rotate}>Rotate</button>
            <button onClick={cancel}>Cancel</button>
        </div>
    )
}