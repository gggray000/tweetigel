import {API} from "./Context.js";
import {useContext} from "react";
import {contentTypeJson} from "./RequestHeaders.js";

function FollowButton({username, followed, func}){
    const api = useContext(API)

    function unfollow(){
        event.preventDefault();
        const payload = {username: username}
        fetch(api+"/user/unfollow",
            {method:"PUT",
                 headers:contentTypeJson(),
                 body: JSON.stringify(payload),
                 credentials: 'include'
            }).then(response => {
                if(!response.ok) {
                    alert("Unable to unfollow");
                    throw new Error("Unable to unfollow");
                }
            }).then(() =>{
                if(func.length === 1){
                    func(username)
                } else {
                    func()
                }
            })
    }

    function follow(){
        event.preventDefault();
        const payload = {username: username}
        fetch(api+"/user/follow",
            {method:"PUT",
                 headers:contentTypeJson(),
                 body: JSON.stringify(payload),
                 credentials: 'include'
            }).then(response => {
                if(!response.ok) {
                    alert("Unable to follow");
                    throw new Error("Unable to follow");
                }
        }).then(() =>{
            if(func.length === 1){
                func(username)
            } else {
                func()
            }
        })
    }

    if(followed===true){
        return <>
            <button className="pico-background-zinc-500" onClick={unfollow}>Unfollow</button>
        </>
    } else if(followed===false){
        return <>
            <button className="pico-background-jade-350" value="Follow" onClick={follow}>Follow</button>
        </>
    } else {
        return <></>
    }
}

export default FollowButton