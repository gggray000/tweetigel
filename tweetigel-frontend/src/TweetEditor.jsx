import {useContext, useEffect, useRef, useState} from "react";
import {API} from "./Context.js";
import {contentTypeJson} from "./RequestHeaders.js";

function TweetEditor(){
    const api = useContext(API)
    const content = useRef(undefined)
    const [posted, setPosted] = useState(false)

    useEffect(() => {
        setPosted(false)
    }, [])

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
                    alert("Unable to launch post.");
                }else{
                    content.current.value=""
                    setPosted(true)
                    setTimeout(() => setPosted(false), 3000);
                }
        })
    }

    return <>
            <div className="editor-container">
                {posted === true?
                        <ul>
                         <textarea
                             name="posted"
                             aria-invalid="false"
                             aria-describedby="posted-helper"
                             placeholder="Enter content here." ref={content}></textarea>
                            <small id="posted-helper">Posted successfully!</small>
                        </ul>
                    :  <ul>
                            <textarea placeholder="Enter content here." ref={content}></textarea>
                        </ul>
                }
                <ul>
                    <button className="pico-background-jade-350" onClick={createPost}>Post</button>
                </ul>
            </div>
        </>
}

export default TweetEditor