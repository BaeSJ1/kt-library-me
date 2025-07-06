import React, { useState, useEffect } from 'react';
import styles from '../styles/SubscriptionPage.module.css';
import { customerAPI } from '../services/api';

const SubscriptionPage = () => {
  const [message, setMessage] = useState('');
  const [user, setUser] = useState(null);
  const [subscriptionId, setSubscriptionId] = useState('');

  useEffect(() => {
    const stored = localStorage.getItem('user');
    if (stored) {
      const parsed = JSON.parse(stored);
      setUser(parsed);
    }
  }, []);
   useEffect(() => {
    if (subscriptionId) {
      setMessage(`구독 성공: ID ${subscriptionId}`);
    }
  }, [subscriptionId]);

//구독
 const handleSubscribe = async () => {
    try {
      const response = await customerAPI.subscribe(Number(user.id));
      const newId = response.id;
      setSubscriptionId(newId);
      setMessage(`구독 성공: ID ${newId}`);
    } catch (error) {
      console.error(error);
      if (error.message) {
        setMessage(`구독 실패: ${error.message}`);
      } else {
        setMessage('구독 실패: 알 수 없는 오류');
      }
    }
  };

  // 구독 취소
  const handleCancel = async () => {
    try {
      await customerAPI.cancelSubscription(subscriptionId, Number(user.id));
      setSubscriptionId('');
      setMessage('구독 취소 성공');
    } catch (error) {
      console.error(error);
      setMessage(`구독 취소 실패: ID ${subscriptionId}`);
    }
  };

  if (!user) return <div>로딩 중...</div>;

  return (
    <div className={styles.subscriptionContainer}>
      <div className={styles.subscriptionCard}>
        <h1 className={styles.title}>📚 구독 관리</h1>

        <div className={styles.userInfo}>
          <p><strong>사용자:</strong> {user.name}</p>
          <p><strong>이메일:</strong> {user.email}</p>
          <p><strong>현재 상태:</strong> {subscriptionId ? '구독 중' : '미구독'}</p>
        </div>

        <div className={styles.buttonGroup}>
          <button className={styles.purchaseButton} onClick={handleSubscribe}>
            구독권 구매
          </button>
          <button className={styles.cancelButton} onClick={handleCancel}>
            구독권 취소
          </button>
        </div>

        {message && <div className={styles.message}>{message}</div>}
      </div>
    </div>
  );

   
};

export default SubscriptionPage;
