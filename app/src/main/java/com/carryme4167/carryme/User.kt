package com.carryme4167.carryme

class User(val username: String, val email: String, val password: String, val phone: String, val nid: String, val category: String, val drivingLicence: String)
{
    constructor():this("", "", "", "", "", "", "")
}