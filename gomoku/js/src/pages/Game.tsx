import * as React from 'react'

export function Game() : React.ReactElement {

    const contents = localStorage.getItem("accessToken") == null ? <div></div> : renderGame()

    return(
        <div>
            {contents}
        </div>
    )

    function renderGame() : React.ReactElement { 
        return
    }
}