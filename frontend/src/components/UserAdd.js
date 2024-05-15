import React, { useState } from 'react';
import api from '../services/api';

/**
 * Represents a component for adding a user.
 *
 * @returns {JSX.Element} The rendered component.
 */
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
            if (response.data.id) {
                // Storing user id in local storage
                localStorage.setItem('userId', response.data.id);
                alert("User saved successfully.");
            }
        } catch (error) {
            console.error('Error:', error);
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
