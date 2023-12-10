import * as React from 'react'

export type Player = {
    username : string,
    numberOfGames : number,
    numberOfWins : number,
}

export function PlayerStats({username, numberOfGames, numberOfWins} : Player): React.ReactElement{
    let winRate = (numberOfWins/numberOfGames) * 100
    return (
        <div className="player-stats">
            <p>Username: {username}</p>
            <p>Number Of Games: {numberOfGames}</p>
            <p>Number Of Wins : {numberOfWins}</p>
            <p>Win Rate : {winRate.toFixed(2)}%</p>
        </div>
    ) 
}