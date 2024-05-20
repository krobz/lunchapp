import React, { useState } from 'react';
import api from '../services/api';

const UserAdd = () => {
    const [user, setUser] = useState({ name: "", email: "" });

    const onChangeHandler = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    };

    const onSubmitHandler = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/users', user);
            const token = response.headers['authorization'] ? response.headers['authorization'].split(' ')[1] : null;
            if (token) {
                localStorage.setItem('jwt', token);
                localStorage.setItem('userId', response.data.id);
                alert(`User saved successfully.`);
            } else {
                alert("User saved but no login token received.");
            }
        } catch (error) {
            console.error('Error:', error.response ? error.response.data : error.message);
            alert('Failed to save user. Please try again.');
        }
    };

    return (
        <div className="container">
            <form onSubmit={onSubmitHandler}>
                <label>Name:
                    <input type="text" name="name" value={user.name} onChange={onChangeHandler} />
                </label>
                <label>Email:
                    <input type="email" name="email" value={user.email} onChange={onChangeHandler} />
                </label>
                <button type="submit">Add User</button>
            </form>
        </div>
    );
};

export default UserAdd;