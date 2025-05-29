//import {useContext} from "react";
//import {API} from "./Context.js";
import TweetFeed from "./TweetFeed.jsx";

function UserProfile({viewingUsername, setViewingUsername, setView}){
    //const api = useContext(API)

    function goBack(){
        setView("loggedIn")
        setViewingUsername(null)
    }

    return <>
        <input type="button" value="Go Back to Feed" onClick={goBack}/>
        <h3>User Profile - {viewingUsername}</h3>
        <details name="full name" open>
            <summary>Full Name</summary>
            <p>full full name</p>
        </details>
        <details name="email" open>
            <summary>Email</summary>
            <p>olalalala@email.com</p>
        </details>
        <details name="biography" open>
            <summary>Biography</summary>
            <p>uh huh uh huh bla bla bla</p>
        </details>

        <h3>Posts</h3>
        <TweetFeed viewingUsername={viewingUsername}></TweetFeed>
    </>

}

export default UserProfile