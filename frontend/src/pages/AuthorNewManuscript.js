// src/pages/AuthorNewManuscript.js
import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { manuscriptAPI } from '../services/api';
import { authorAPI } from '../services/api';

const AuthorNewManuscript = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user'));
  
  // location.state에서 전달받은 데이터 확인
  const { manuscript, isEdit } = location.state || {};
  
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  // 수정 모드일 때 기존 데이터로 폼 초기화
  useEffect(() => {
    if (isEdit && manuscript) {
      setTitle(manuscript.manuscriptTitle || '');
      setContent(manuscript.manuscriptContent || '');
    }
  }, [isEdit, manuscript]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    
    try {
      const author = await authorAPI.getAuthor(user.id);
      if (!author.isApproved) {
        alert('승인되지 않은 작가는 원고를 등록할 수 없습니다.');
        return; // 등록 중단
      }

      const manuscriptData = {
        authorId: user.id,
        authorName: user.name,
        authorIntroduction: user.introduction,
        manuscriptTitle: title,
        manuscriptContent: content,
      };

      if (isEdit && manuscript) {
        // 수정 모드: editManuscript API 호출
        await manuscriptAPI.editManuscript(manuscript.id, manuscriptData);
        alert('원고 수정 성공!');
      } else {
        // 새 원고 모드: registerManuscript API 호출
        await manuscriptAPI.registerManuscript(manuscriptData);
        alert('원고 등록 성공!');
        setTitle('');
        setContent('');
      }
      
      // 성공 후 원고 목록 페이지로 이동
      navigate('/author/manuscripts');
    } catch (err) {
      alert(`${isEdit ? '수정' : '등록'} 실패: ${err.message}`);
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>{isEdit ? '원고 수정' : '새 원고'}</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>제목</label><br />
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            style={{ width: '100%', padding: '0.5rem' }}
          />
        </div>
        <div style={{ marginTop: '1rem' }}>
          <label>내용</label><br />
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
            rows={10}
            style={{ width: '100%', padding: '0.5rem' }}
          />
        </div>
        <div style={{ marginTop: '1rem' }}>
          <button type="submit" style={{ marginTop: '1rem', padding: '0.5rem 1rem', marginRight: '0.5rem' }}>
            {isEdit ? '수정 완료' : '등록'}
          </button>
          <button 
            type="button" 
            onClick={() => navigate('/author/manuscripts')}
            style={{ padding: '0.5rem 1rem', backgroundColor: '#6b7280', color: 'white', border: 'none', borderRadius: '0.25rem' }}
          >
            취소
          </button>
        </div>
      </form>
    </div>
  );
};

export default AuthorNewManuscript;
