/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.domain;

import com.esofthead.mycollab.core.arguments.ValuedBean;

public class RoleUser extends ValuedBean {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_roleuser.id
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_roleuser.username
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    private String username;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_roleuser.roleid
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    private Integer roleid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_roleuser.id
     *
     * @return the value of s_roleuser.id
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_roleuser.id
     *
     * @param id the value for s_roleuser.id
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_roleuser.username
     *
     * @return the value of s_roleuser.username
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_roleuser.username
     *
     * @param username the value for s_roleuser.username
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_roleuser.roleid
     *
     * @return the value of s_roleuser.roleid
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    public Integer getRoleid() {
        return roleid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_roleuser.roleid
     *
     * @param roleid the value for s_roleuser.roleid
     *
     * @mbggenerated Tue Jan 15 16:44:21 GMT+07:00 2013
     */
    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }
}