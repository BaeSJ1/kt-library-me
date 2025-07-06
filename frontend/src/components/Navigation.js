import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import styles from '../styles/Navigation.module.css';

const Navigation = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [user, setUser] = useState(null);
  const [activeMenu, setActiveMenu] = useState('');

  // 임시 사용자 데이터 (실제로는 Context나 Redux에서 가져올 것)
  useEffect(() => {
    // 임시로 localStorage에서 사용자 정보를 가져옴
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  useEffect(() => {
    setActiveMenu(location.pathname);
  }, [location.pathname]);

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
    navigate('/');
  };

  const authorMenus = [
    {
      path: '/author/manuscripts',
      label: '내 원고',
      icon: 'description'
    },
    {
      path: '/author/new-manuscript',
      label: '새 원고',
      icon: 'add_circle'
    },
    {
      path: '/author/publishing',
      label: '출판 현황',
      icon: 'analytics'
    },
    {
      path: '/author/profile',
      label: '작가 정보',
      icon: 'person'
    }
  ];

  const customerMenus = [
    {
      path: '/customer/books',
      label: '도서 둘러보기',
      icon: 'library_books'
    },
    {
      path: '/customer/bookshelf',
      label: '내 서재',
      icon: 'bookmarks'
    },
    {
      path: '/customer/subscription',
      label: '구독 관리',
      icon: 'card_membership'
    },
    {
      path: '/customer/points',
      label: '포인트',
      icon: 'stars'
    }
  ];

  const adminMenus = [
    {
      path: '/admin/authors',
      label: '작가 승인',
      icon: 'how_to_reg'
    },
    {
      path: '/admin/books',
      label: '도서 관리',
      icon: 'library_books'
    },
    {
      path: '/admin/customers',
      label: '고객 관리',
      icon: 'people'
    }
  ];

  const getMenusByRole = () => {
    if (!user) return [];
    
    switch (user.role) {
      case 'author':
        return authorMenus;
      case 'customer':
        return customerMenus;
      case 'admin':
        return adminMenus;
      default:
        return [];
    }
  };

  const getRoleDisplayName = (role) => {
    switch (role) {
      case 'author':
        return '작가';
      case 'customer':
        return '고객';
      case 'admin':
        return '관리자';
      default:
        return '사용자';
    }
  };

  return (
    <nav className={styles.navigation}>
      <div className={styles.navigationContainer}>
        <div className={styles.navigationContent}>
          <div className={styles.navigationLeft}>
            <div className={styles.navigationLogo} onClick={() => {
              if (user && user.role === 'admin') {
                navigate('/admin/authors');
              } else {
                navigate('/');
              }
            }}>
              <div className={styles.logoIcon}>
                <span className="material-icons">menu_book</span>
              </div>
              <span className={styles.logoText}>KT Library</span>
            </div>
            
            {user && (
              <div className={styles.navigationButtons}>
                {getMenusByRole().map((menu) => (
                  <button
                    key={menu.path}
                    className={`${styles.navButton} ${activeMenu === menu.path ? styles.active : ''}`}
                    onClick={() => navigate(menu.path)}
                  >
                    <span className="material-icons">{menu.icon}</span>
                    <span>{menu.label}</span>
                  </button>
                ))}
              </div>
            )}
          </div>

          <div className={styles.navigationRight}>
            {user ? (
              <>
                <div className={styles.userInfo}>
                  <div className={styles.userDetails}>
                    <div className={styles.userName}>{user.name}</div>
                    <div className={`${styles.userRole} ${styles[user.role]}`}>
                      {getRoleDisplayName(user.role)}
                    </div>
                  </div>
                </div>
                <button className={styles.logoutButton} onClick={handleLogout}>
                  <span className="material-icons">logout</span>
                  <span>로그아웃</span>
                </button>
              </>
            ) : (
              <div className={styles.guestButtons}>
                <button 
                  className={styles.loginButton}
                  onClick={() => navigate('/login')}
                >
                  로그인
                </button>
                <button 
                  className={styles.signupButton}
                  onClick={() => navigate('/register')}
                >
                  회원가입
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navigation;