import{API} from "./Context.js";
import {useContext, useEffect, useState} from "react";
import CommentSection from "./CommentSection.jsx";

function TweetFeedWithPagination({username, viewingUsername, setViewingUsername, setView}){
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
                    setChanged(!changed);
                }
            }
        )
    }

    function deletePost(id){
        event.preventDefault();
        if(confirm("Are you sure to delete this post?")){
            fetch(api+"/post/" + id, {
                method: 'Delete',
                credentials: 'include'
            }).then(response => {
                    if (!response.ok) {
                        alert("Unable to delete the post: " + response.statusText)
                    } else {
                        setChanged(!changed);
                    }
                }
            )
        }
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
                <button className="outline" onClick={lastPage}>❮ Last Page</button>
                <button className="outline" onClick={nextPage}>Next Page ❯</button>
            </div>
            <ul>
                {feed.map(
                    post => (
                        <article key={post.id}>
                            <header>
                                <div className="grid">
                                    <a href="#" onClick={() => showProfile(post.author.username)}>
                                        <b>{post.author.username}</b>
                                    </a>
                                    <p><small><i>At {post.timestamp}</i></small></p>
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
                                    { username === viewingUsername?
                                        <button className="pico-background-zinc-500" onClick={() => deletePost(post.id)}>
                                            Delete ✖ </button>
                                        : <></>
                                    }
                                </div>
                            </header>
                            {post.content}
                            <footer>
                                    <CommentSection commentsCount={post.commentsCount} postId={post.id}
                                                    onCommentAdded={() => {
                                                        setFeed(feed.map(p => p.id === post.id
                                                            ? { ...p, commentsCount: p.commentsCount + 1 }
                                                            : p))
                                                    }}
                                    ></CommentSection>
                            </footer>
                        </article>
                    ))
                }
            </ul>
        </>
    }
}

export default TweetFeedWithPagination