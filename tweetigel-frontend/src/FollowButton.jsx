import {API} from "./Context.js";
import {useContext} from "react";
import {contentTypeJson} from "./RequestHeaders.js";

function FollowButton({username, followed, result, setResult}){
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
                const newStatus = false;
                const index = result.findIndex(user => user.username === username);
                const newResult = result.slice();
                newResult[index].followed = newStatus
                setResult(newResult)
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
            const newStatus = true;
            const index = result.findIndex(user => user.username === username);
            const newResult = result.slice();
            newResult[index].followed = newStatus
            setResult(newResult)
        })
    }

    if(followed){
        return <>
            <input type="button" value="Unfollow" onClick={unfollow}/>
        </>
    } else {
        return <>
            <input type="button" value="Follow" onClick={follow}/>
        </>
    }
}

export default FollowButton