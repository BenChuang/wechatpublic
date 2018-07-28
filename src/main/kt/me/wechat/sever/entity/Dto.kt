package me.wechat.sever.entity

import java.util.*

data class AccessToken(var accessToken: String = "",
                       var expiresIn: Int = 0,
                       var refreshToken: String = "",
                       var openid: String = "",
                       var scope: String = "")

data class UserInfo(var openid: String = "",
                    var nickname: String = "",
                    var sex: Int = -1,
                    var province: String = "",
                    var city: String = "",
                    var country: String = "",
                    var headimgurl: String = "",
                    var privilege: Array<String> = Array(0, { "" }),
                    var unionid: String = "") {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserInfo

        if (openid != other.openid) return false
        if (nickname != other.nickname) return false
        if (sex != other.sex) return false
        if (province != other.province) return false
        if (city != other.city) return false
        if (country != other.country) return false
        if (headimgurl != other.headimgurl) return false
        if (!Arrays.equals(privilege, other.privilege)) return false
        if (unionid != other.unionid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = openid.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + sex
        result = 31 * result + province.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + headimgurl.hashCode()
        result = 31 * result + Arrays.hashCode(privilege)
        result = 31 * result + unionid.hashCode()
        return result
    }
}