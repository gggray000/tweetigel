function HashTag({content, setView, setHashtag}){

    function showHashTag(){
        setView("hashtag")
        setHashtag(content.replace("#", ""))
    }

    return <>
        <a href="#" onClick={showHashTag}>
            <b><i>{content}</i></b>
        </a>
    </>
}

export default HashTag;