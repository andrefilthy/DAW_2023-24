import * as React from 'react'
import {useState, useEffect} from 'react'
import { TopBar } from '../components/TopBar'
import { fetchInfo } from '../ApiCalls'
import { Author, AuthorCard } from '../components/AuthorCard'
import '../info.css'

export type LinkRelation = {
    rel : string[],
    href : string
}

type InfoProps = {
    name : string,
    description : string,
    version : string,
    authors : Author[]
}



type Props = {
    home : LinkRelation,
    links : LinkRelation[],
    properties : InfoProps
}

export function Info(): React.ReactElement {
    const [props, setProps] = useState<Props | null>(null)
    

    useEffect(() => {
        const fetchData = async () => {
            const data = await fetchInfo()
            const sirenobj = await data.json()
            const arr = sirenobj.links as LinkRelation[]
            const self = arr.find(e => e.rel.includes("self"))
            setProps({
                home : arr.splice(arr.indexOf(self))[0],
                links : arr,
                properties : sirenobj.properties as InfoProps
            })
          }

        fetchData()
    },[])
    return (
        <div>
            {props && <div>
                {<TopBar home={props.home} links={props.links} />}
                <h1>Info</h1>
                <div>
                <h3>{props.properties.name}</h3>
                <p>description : {props.properties.description}</p>
                <p>version : {props.properties.version}</p>
                </div>
                <h3>Authors</h3>
                <span>
                {props.properties.authors.map(a =>
                    <div className='author-container' key={a.studentNumber}>
                        <AuthorCard {...a} ></AuthorCard>
                    </div>
                )}
                </span>
            </div>}
        </div>
    )
}