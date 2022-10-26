package com.project.Week_Mission.app.postkeyword.repository;


import com.project.Week_Mission.app.postkeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByAuthorId(Long authorId);
}
