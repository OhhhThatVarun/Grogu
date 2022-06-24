package com.varun.grogu.domain.entities

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FilmTest {

    @Test
    fun testGetDescriptionFirstPara() {
        var film = Film("", "Luke Skywalker has returned to\r\nhis home planet of Tatooine in\r\nan attempt to rescue his\r\nfriend Han Solo from the\r\nclutches of the vile gangster\r\nJabba the Hutt.\r\n\r\nLittle does Luke know that the\r\nGALACTIC EMPIRE has secretly\r\nbegun construction on a new\r\narmored space station even\r\nmore powerful than the first\r\ndreaded Death Star.\r\n\r\nWhen completed, this ultimate\r\nweapon will spell certain doom\r\nfor the small band of rebels\r\nstruggling to restore freedom\r\nto the galaxy...", "")
        Assert.assertEquals("Luke Skywalker has returned to\r his home planet of Tatooine in\r an attempt to rescue his\r friend Han Solo from the\r clutches of the vile gangster\r Jabba the Hutt.", film.getDescriptionFirstPara())

        film = film.copy(fullDescription = "It is a dark time for the\r\nRebellion. Although the Death\r\nStar has been destroyed,\r\nImperial troops have driven the\r\nRebel forces from their hidden\r\nbase and pursued them across\r\nthe galaxy.\r\n\r\nEvading the dreaded Imperial\r\nStarfleet, a group of freedom\r\nfighters led by Luke Skywalker\r\nhas established a new secret\r\nbase on the remote ice world\r\nof Hoth.\r\n\r\nThe evil lord Darth Vader,\r\nobsessed with finding young\r\nSkywalker, has dispatched\r\nthousands of remote probes into\r\nthe far reaches of space....")
        Assert.assertEquals("It is a dark time for the\r Rebellion. Although the Death\r Star has been destroyed,\r Imperial troops have driven the\r Rebel forces from their hidden\r base and pursued them across\r the galaxy.", film.getDescriptionFirstPara())
    }
}