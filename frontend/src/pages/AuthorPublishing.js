// src/pages/AuthorPublishing.js
import React, { useEffect, useState } from 'react';
import { manuscriptAPI } from '../services/api';
import styles from '../styles/AuthorPublishing.module.css'; // 스타일 분리

const AuthorPublishing = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  const [published, setPublished] = useState([]);

  useEffect(() => {
    const fetch = async () => {
      try {
        const response = await manuscriptAPI.getAllManuscripts();
        const all = response._embedded?.manuscripts ?? [];
        const mine = all.filter((m) => m.authorId === user.id);
        const publishedList = mine.filter((m) => !!m.authorName);
        setPublished(publishedList);
      } catch (err) {
        console.error('출판현황 조회 실패:', err.message);
      }
    };
    fetch();
  }, []); //useEffect 1회 실행

  return (
    <div className={styles.container}>
      <h1>출판 현황</h1>
      {published.length === 0 ? (
        <p>출판을 요청한 원고가 없습니다.</p>
      ) : (
        <div className={styles.cardGrid}>
          {published.map((m) => (
            <div key={m.id} className={styles.card}>
              <h2 className={styles.title}>{m.manuscriptTitle}</h2>
              <p className={styles.content}>{m.manuscriptContent}</p>
              <p className={styles.author}>작가: {m.authorName}</p>
              <p className={styles.status}>출판 요청 완료 ✅</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AuthorPublishing;
