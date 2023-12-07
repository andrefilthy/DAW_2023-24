/*
import * as React from 'react'
import {
    useState,
    useEffect,
} from 'react'

type TimerProps = {

}
export function Timer({ }: TimerProps): React.ReactElement {
    const [counter, setCounter] = useState(0)
    const [period, setPeriod] = useState(1000)
    useEffect(() => {
        console.log(`Effect is running with period=${period}`)
        const tid = setInterval(
            () => setCounter((oldState) => oldState + 1),
            period,
        )
        return () => {
            console.log(`Cancel effect with period=${period}`)
            clearInterval(tid)
        }
    }, [period, setCounter])

    console.log("Timer is returning")
    return (
        <div>
            <span>{counter}</span>
            <button onClick={() => setPeriod(period-500)}>-</button>
            {period}
            <button onClick={() => setPeriod(period+500)}>+</button>
        </div>
    )
}
*/

import * as React from 'react'
import {
    useState,
    useEffect,
} from 'react'
import { GameState } from '../pages/Game';

type TimerProps = {
    time : number,
    shootingTurn? : GameState
}


function displayTime(minutes){
    var sign = minutes < 0 ? "-" : "";
    var min = Math.floor(Math.abs(minutes));
    var sec = Math.floor((Math.abs(minutes) * 60) % 60);
    return sign + (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
}

export function Timer({time, shootingTurn} : TimerProps)  : React.ReactElement{
    const [counter, setCounter] = useState(time)
    const period = 1000
    var minutes = counter/60
    useEffect(() => {
        const tid = setInterval(
            () => setCounter((oldState) => oldState - 1),
            period,
        )
        return () => {
            console.log(`Cancel effect with period=${period}`)
            clearInterval(tid)
        }
    }, [period, setCounter])

    useEffect(() => {
        console.log("in setCounter useEffect")
        setCounter(time)
    },[shootingTurn])

    return (
        <div>
            <span>{ displayTime(minutes)}</span>
        </div>
    )
}