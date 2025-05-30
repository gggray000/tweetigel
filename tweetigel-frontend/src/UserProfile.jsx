import {useContext, useEffect, useRef, useState} from "react";
import {API} from "./Context.js";
import TweetFeed from "./TweetFeed.jsx";
import {contentTypeJson} from "./RequestHeaders.js";

function UserProfile({ username, viewingUsername, setViewingUsername, setView}){
    const api = useContext(API)
    const [editable, setEditable] = useState(false)
    const [editing, setEditing] = useState(false)
    const [profile, setProfile] = useState({})
    const fullName = useRef(undefined)
    const email = useRef(undefined)
    const biography = useRef(undefined)

    useEffect(() => {
        fetch(api + "/getProfile/" + viewingUsername.toString(),{
            method:"GET",
            credentials: 'include'
        }).then(response => {
            if(!response.ok){
                alert(response.statusText)
                return null
            }else{
                return response.json()
            }
        }).then(parsedResponse => {
            setProfile(parsedResponse)
            if(username.toString() === viewingUsername.toString()){
                setEditable(true)
                setEditing(false)
            }
        })
    },[api, viewingUsername])


    function goBack(){
        setView("loggedIn")
        setViewingUsername(null)
    }

    function editProfile(){
        setEditing(true)
    }

    function updateProfile(){
        event.preventDefault();
        const payload = {
            fullName: fullName.current.value,
            email: email.current.value,
            biography: biography.current.value
        }
        fetch(api + "/updateProfile",{
            method: "PUT",
            headers: contentTypeJson(),
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(response => {
            if(!response.ok){
                alert(response.statusText)
                return null
            }else{
                return response.json()
            }
        }).then(parsedResponse => {
            setProfile(parsedResponse)
            setEditable(true)
            setEditing(false)
        })
    }

    return <>
        <div>
            <button className="pico-background-azure-450" onClick={goBack}>Back to Feed</button>
        </div>
        <br />
        <div>
            <h3>User Profile - {viewingUsername}</h3>
        </div>

        <div>
            <label><small>Registered At: {profile?profile.registeredAt:NaN}</small></label>
            <label><small>Followed: {profile?profile.followedNum:NaN}</small></label>
            <label><small>Follower: {profile?profile.followersNum:NaN}</small></label>
        </div>
        <hr />

        {editable === true && editing === false
            ?<div>
                <details name="full name" open>
                    <summary>Full Name</summary>
                    <p>{profile.fullName}</p>
                </details>
                <details name="email" open>
                    <summary>Email</summary>
                    <p>{profile.email}</p>
                </details>
                <details name="biography" open>
                    <summary>Biography</summary>
                    <p>{profile.biography}</p>
                </details>
                <button className="pico-background-jade-350" onClick={editProfile}>Edit Profile</button>
                <hr />
            </div>
                : editable === true && editing === true ?
                <div>
                    <form onSubmit={updateProfile}>
                        <fieldset>
                            <label>
                                Full Name
                                <input
                                    name="fullName"
                                    placeholder="Full Name"
                                    ref = {fullName}
                                    defaultValue={profile.fullName || ""}/>
                            </label>
                            <label>
                                Email
                                <input
                                    type="email"
                                    name="email"
                                    placeholder="Email"
                                    autoComplete="email"
                                    ref = {email}
                                    defaultValue={profile.email || ""}/>
                            </label>
                            <label>
                                Biography
                            </label>
                                <textarea
                                    name="biography"
                                    placeholder="Introduce yourself to other tweetIgels..."
                                    aria-label="biography"
                                    ref = {biography}
                                    defaultValue={profile.biography || ""}/>
                        </fieldset>
                        <input className="pico-background-jade-350"
                               type="submit"
                               value="Update"
                        />
                        <hr />
                    </form>
                </div>
                : <div>
                    <details name="full name" open>
                        <summary>Full Name</summary>
                        <p>{profile.fullName}</p>
                    </details>
                    <details name="email" open>
                        <summary>Email</summary>
                        <p>{profile.email}</p>
                    </details>
                        <details name="biography" open>
                            <summary>Biography</summary>
                            <p>{profile.biography}</p>
                        </details>
                    </div>
        }
        <h3>Posts</h3>
        <TweetFeed viewingUsername={viewingUsername} />
    </>

}

export default UserProfile