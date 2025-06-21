import {useContext, useEffect, useState} from "react";
import {API} from "./Context.js";
import Post from "./Post.jsx";

function HashTagFeed({hashtag, setHashtag, setView, username, setViewingUsername}){
    const api = useContext(API)
    const [feed, setFeed] = useState([])
    const [changed, setChanged] = useState(false)


    useEffect(() => {
        fetch(api + "/hashtag/" + hashtag, {
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
        })
    },[api, changed])

    function goBack(){
        setView("loggedIn")
        setViewingUsername(null)
    }

    return <>
        <div>
            <button className="pico-background-azure-450" onClick={goBack}>Back to Feed</button>
            <br/>
            <br/>
        </div>
        {feed.length === 0? <>
                <h5>No posts under this Hashtag.</h5>
            </>
            : <>
                <h3>#{hashtag}</h3>
                <hr />
                <ul>
                    {feed.map(post => (
                        <Post post={post} setView={setView} username={username}
                              changed={changed} setChanged={setChanged}
                              setViewingUsername={setViewingUsername} setHashtag={setHashtag}
                              feed={feed} setFeed={setFeed} >
                        </Post>)
                    )}
                </ul>
            </>
        }
    </>
}

export default HashTagFeed;