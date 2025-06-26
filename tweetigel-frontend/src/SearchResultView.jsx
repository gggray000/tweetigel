import FollowButton from "./FollowButton.jsx";
import TweetFeedWithPagination from "./TweetFeedWithPagination.jsx";
import Post from "./Post.jsx";
import {useContext, useEffect, useState} from "react";
import {API} from "./Context.js";

function SearchResultView({view, setView, setViewingUsername, username, setHashtag, searchTerm}){
    const api = useContext(API)
    const [changed, setChanged] = useState(false)
    const [result, setResult] = useState([])

        useEffect(() => {
            if(view === "search-user"){
                fetch(api+ "/user/search?term=" + encodeURIComponent(searchTerm),
                    {method:"GET",
                        credentials: 'include'
                    }).then(response => {
                    if(!response.ok) {
                        alert("Unable to Search");
                        return []
                    }else{
                        return response.json()
                    }
                }).then(result => {
                    setResult(result)
                })
            }
        },[changed])

        function showProfile(username){
            setView("profile")
            setViewingUsername(username)
        }

        function goBack(){
            setView("loggedIn")
        }

        function followButtonOp(username){
            const index = result.findIndex(user => user.username === username);
            const oldStatus = result[index].followed;
            const newResult = result.slice();
            newResult[index].followed = !oldStatus
            setResult(newResult)
        }

        if (view === "search-user"){
            return <>
                <div className="search-header">
                    {result.length === 0? <h3>No user found.</h3> :<h3>User Search Result For: "{searchTerm}"</h3>}
                    <br/>
                    <button className="pico-background-azure-450" onClick={goBack}>Go Back to Feed</button>
                </div>
                <br/>
                {result.length === 0? <></>
                    : <table className="search-table">
                        <thead>
                        <tr>
                            <th scope="col"><h5>Username</h5></th>
                            <th scope="col"><h5>Followed</h5></th>
                            <th scope="col"><h5>Action</h5></th>
                        </tr>
                        </thead>
                        <tbody>
                        {result.map(user => (
                            <tr key={user.username}>
                                <th scope="row" >
                                    <i>{user.username}</i>
                                    <a href="#" onClick={() => showProfile(user.username)} > <sub><i>Profile</i></sub></a>
                                </th>
                                <td>{user.followed?"✅":"❌"}</td>
                                <td>{user.followed?
                                    <FollowButton username={user.username} followed={true} func={followButtonOp} />
                                    : <FollowButton username={user.username} followed={false} func={followButtonOp}/>}
                                </td>
                            </tr>
                        ))
                        }
                        </tbody>
                    </table>
                }
            </>
        } else {
            return<>
                <div className="search-header">
                   <h3>Post Search Result For: "{searchTerm}"</h3>
                    <br/>
                </div>
                <br/>
                <TweetFeedWithPagination username={username} viewingUsername={null} setViewingUsername={setViewingUsername} setView={setView} hashtag={null} setHashtag={setHashtag} changed={changed} setChanged={setChanged} searchTerm={searchTerm} type={"search"}/>

            </>
        }

}

export default SearchResultView