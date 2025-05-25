import {useContext, useRef} from "react";
import {API} from "./Context.js";
import {contentTypeJson} from "./RequestHeaders.js";

function TweetEditor(){
    const api = useContext(API)
    const content = useRef(undefined)

    function createPost(){
        event.preventDefault();
        if(content.current.value === "" || content.current.value===undefined){
            alert("Post can't be empty!")
            return
        }
        const payload = {content:content.current.value}
        fetch(api+"/post", {
            method:'POST',
            headers: contentTypeJson(),
            body: JSON.stringify(payload),
            credentials: 'include'
        }).then(response => {
                if(!response.ok){
                    alert("Post Failed.");
                }else{
                    alert("Post successfully!")
                    content.current.value=""
                }
        })
    }

    return <>
        <ul>
            <textarea placeholder="Enter content here." ref={content}></textarea>
        </ul>
        <ul>
            <form onSubmit={createPost}>
                <input type="submit" value="Post!"/>
            </form>

        </ul>
    </>
}

export default TweetEditor