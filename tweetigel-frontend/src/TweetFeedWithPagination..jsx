import{API} from "./Context.js";
import {useContext, useEffect, useState} from "react";
import Post from "./Post.jsx"
import CommentSection from "./CommentSection.jsx";

function TweetFeedWithPagination({username, viewingUsername, setViewingUsername, setView, setHashtag}){
    const api = useContext(API)
    const [feed, setFeed] = useState([])
    const [changed, setChanged] = useState(false)
    const [pageNum, setPageNum] = useState(0)
    const [maxPageNum, setMaxPageNum] = useState(1)

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
                <button className="outline" onClick={lastPage}>❮ Last Page</button>
                <button className="outline" onClick={nextPage}>Next Page ❯</button>
            </div>
            <ul>
                {feed.map(post => (
                        <Post key={post.id} post={post} setView={setView} username={username}
                              changed={changed} setChanged={setChanged}
                              viewingUsername={viewingUsername} setViewingUsername={setViewingUsername}
                              feed={feed} setFeed={setFeed} setHashtag={setHashtag}
                        />)
                )}
            </ul>
        </>
    }
}

export default TweetFeedWithPagination