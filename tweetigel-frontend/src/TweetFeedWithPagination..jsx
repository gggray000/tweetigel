import{API} from "./Context.js";
import {useContext, useEffect, useState} from "react";

function TweetFeedWithPagination({viewingUsername, setViewingUsername, setView}){
    const api = useContext(API)
    const [feed, setFeed] = useState([])
    const [changed, setChanged] = useState(false)
    const [pageNum, setPageNum] = useState(0)
    const [maxPageNum, setMaxPageNum] = useState(1)

    // api+"/user/search?term=" + searchTerm.current.value,

    useEffect(() => {
        fetch(api + "/posts" + (viewingUsername===null ?"" :"/" + viewingUsername.toString()) + "?" + "page=" + pageNum.toString(), {
            method:"GET",
            credentials: 'include'
        }).then(response => {
            if(!response.ok){
                alert("Unable to load posts list: " + response.statusText)
                return []
            }else{
                return response.json()
            }
        }).then(parsedResponse => {
            setFeed(parsedResponse)
        }).then(() => {
                fetch(api + "/postsCount" + (viewingUsername===null ?"" :"/" + viewingUsername.toString()), {
                    method:"GET",
                    credentials: 'include'
                }).then(response => {
                    if(!response.ok){
                        alert("Unable to get post count: " + response.statusText)
                    } else {
                        return response.json()
                    }
                }).then(count => {
                    setMaxPageNum(Math.floor(count / 20))
                })
        })
    },[api, changed, pageNum, viewingUsername])

    function like(id){
        event.preventDefault();
        fetch(api+"/post/" + id + "/like", {
            method: 'PUT',
            credentials: 'include'
        }).then(response => {
                if (!response.ok) {
                    alert("Unable to like the post: " + response.statusText)
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
                    alert("Unable to unlike the post: " + response.statusText)
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

    function showProfile(username){
        setView("profile")
        setViewingUsername(username)
    }

    function lastPage(){
        if(pageNum > 0){
            setPageNum(pageNum-1)
        }
        else{
            alert("Already at the first page.")
        }
    }

    function nextPage(){
        if(pageNum < maxPageNum){
            setPageNum(pageNum + 1)
        }
        else{
            alert("Already at the last page.")
        }
    }

    if(feed.length === 0){
        return <>
            <h5>The feed is empty.</h5>
        </>
    }else{
        return <>
            <div role="group">
                <button className="pico-background-azure-50" onClick={lastPage}>❮ Last Page</button>
                <button className="pico-background-azure-50" onClick={nextPage}>Next Page ❯</button>
            </div>
            <ul>
                {feed.map(
                    post => (
                        <article key={post.id}>
                            <header>
                                <b>{post.author.username}</b>
                                <a href="#" onClick={() => showProfile(post.author.username)} > <sub><i>    info</i></sub></a>
                            </header>
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

export default TweetFeedWithPagination