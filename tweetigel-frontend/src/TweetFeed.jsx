import{API} from "./Context.js";
import {useContext, useEffect, useState} from "react";

function TweetFeed(){
    const api = useContext(API)
    const [feed, setFeed] = useState([])
    const [changed, setChanged] = useState(false)

    useEffect(() => {
        fetch(api + "/posts", {
            method:"GET",
            credentials: 'include'
        }).then(response => {
            if(!response.ok){
                alert(response.statusText)
                return []
            }else{
                return response.json()
            }
        }).then(parsedResponse => {
            setFeed(parsedResponse)
        })
    },[api, changed])

    function like(id){
        event.preventDefault();
        fetch(api+"/post/" + id + "/like", {
            method: 'PUT',
            credentials: 'include'
        }).then(response => {
                if (!response.ok) {
                    alert(response.statusText)
                } else {
                    const newFeed = feed.slice();
                    const index = feed.findIndex(post => post.id === id)
                    newFeed[index].likesCount = newFeed[index].likesCount + 1;
                    setFeed(newFeed);
                    setChanged(!changed);
                }
            }
        )
    }

    function unlike(id){
        event.preventDefault();
        fetch(api+"/post/" + id + "/unlike", {
            method: 'PUT',
            credentials: 'include'
        }).then(response => {
                if (!response.ok) {
                    alert(response.statusText)
                } else {
                    const newFeed = feed.slice();
                    const index = feed.findIndex(post => post.id === id)
                    newFeed[index].likesCount = newFeed[index].likesCount - 1;
                    setFeed(newFeed);
                    setChanged(!changed);
                }
            }
        )
    }

    if(feed.length === 0){
        return <>
        <h5>The feed is empty.</h5>
        </>
    }else{
        return <>
            <ul>
                {feed.map(
                    post => (
                        <article key={post.id}>
                            <header><b>{post.author.username}</b></header>
                            {post.content}
                            <footer>
                                <div className="grid">
                                    {post.timestamp}
                                    {post.likeable === true?
                                        <button className="pico-background-pink-450" onClick={() => like(post.id)}>
                                            Like ♥ {post.likesCount}
                                        </button>
                                            :post.likeable===false?
                                            <button className="pico-background-zinc-500" onClick={() => unlike(post.id)}>
                                                Unlike ♡ {post.likesCount}
                                            </button>
                                                : <></>
                                    }
                                </div>

                            </footer>
                        </article>
                    ))
                }
            </ul>
        </>
    }
}

export default TweetFeed